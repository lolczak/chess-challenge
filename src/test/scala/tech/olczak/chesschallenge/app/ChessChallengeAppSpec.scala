package tech.olczak.chesschallenge.app

import org.mockito.Matchers.{any, eq => argEq}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import tech.olczak.chesschallenge.ChessObjectMother._
import tech.olczak.chesschallenge.app.ChessChallengeApp.{IO, mainCmd}
import tech.olczak.chesschallenge.app.cli.{CliParser, ParseFailure}
import tech.olczak.chesschallenge.app.effect._
import tech.olczak.chesschallenge.solver.ChessChallengeSolver

import scalaz._

class ChessChallengeAppSpec extends WordSpec with Matchers with MockitoSugar {

  "A chess challenge app" when {

    "starting" should {
      "print greeting" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        run(mainCmd)
        //then
        stdout should contain("Hello, starting chess challenge app...")
      }
    }

    "command line arguments are invalid" should {
      "fail with error code equal 1" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        run(mainCmd)
        //then
        errorCode shouldBe 1
      }

      "print error message" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        run(mainCmd)
        //then
        stderr should contain("Invalid arguments: rank count missing")
        stderr should contain("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      }
    }

    "solutions are found" should {

      "print the total number of unique arrangements" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        run(mainCmd)
        //then
        stdout should contain("Found 4 solutions.")
      }

      "print the time the solver took to find all solutions" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        run(mainCmd)
        //then
        stdout should contain("Elapsed time: 20 sec and 234 millis.")
      }

      "list all solutions to the console" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        run(mainCmd)
        //then
        stdout should contain allOf(
          """K _ K
            |_ _ _
            |_ R _
            |""".stripMargin,
          """_ R _
            |_ _ _
            |K _ K
            |""".stripMargin,
          """K _ _
            |_ _ R
            |K _ _
            |""".stripMargin,
          """_ _ K
            |R _ _
            |_ _ K
            |""".stripMargin
        )
      }

    }

  }

  trait TestContext {

    val cliParser = mock[CliParser]

    val solver = mock[ChessChallengeSolver]

    lazy val env = Environment(List("3", "3", "K2", "R1"), cliParser, solver)

    var buffer = Buffer(millis = Stream.iterate(0l)(_ + 20234l))

    def run[A](cmd: ReaderT[Free[IO, ?], Environment, A]): A = {
      val (buf, result) = cmd.run(env).foldMap(ConsoleToState or SystemToState).run(buffer)
      buffer = buf
      result
    }

    def stdout: List[String] = buffer.stdout.reverse

    def stderr: List[String] = buffer.stderr.reverse

    def errorCode: Int = buffer.exitCode.getOrElse(0)

  }

}
