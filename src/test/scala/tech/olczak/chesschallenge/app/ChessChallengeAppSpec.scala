package tech.olczak.chesschallenge.app

import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import tech.olczak.chesschallenge.ChessObjectMother._
import tech.olczak.chesschallenge.app.ChessChallengeApp.{IO, mainAction}
import tech.olczak.chesschallenge.app.cli.{CliParser, ParseFailure}
import tech.olczak.chesschallenge.app.effect._
import tech.olczak.chesschallenge.app.interpreter.{ConsoleToState, RuntimeState, SystemToState}
import tech.olczak.chesschallenge.solver.ChessChallengeSolver

import scalaz._

class ChessChallengeAppSpec extends WordSpec with Matchers with MockitoSugar {

  "A chess challenge app" when {

    "starting" should {
      "print greeting" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        val runtime = run(mainAction)
        //then
        runtime.stdout should contain("Hello, starting chess challenge app...")
      }
    }

    "command line arguments are invalid" should {
      "fail with error code equal 1" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        val runtime = run(mainAction)
        //then
        runtime.maybeExitCode shouldBe Some(1)
      }

      "print error message" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        val runtime = run(mainAction)
        //then
        runtime.stderr should contain("Invalid arguments: rank count missing")
        runtime.stderr should contain("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      }
    }

    "solutions are found" should {

      "print the total number of unique arrangements" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        val runtime = run(mainAction)
        //then
        runtime.stdout should contain("Found 4 solutions.")
      }

      "print the time the solver took to find all solutions" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        val runtime = run(mainAction)
        //then
        runtime.stdout should contain(s"Elapsed time: ${TestClockTick / 1000} sec and ${TestClockTick % 1000} millis.")
      }

      "list all solutions to the console" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        val runtime = run(mainAction)
        //then
        runtime.stdout should contain allOf(
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

    val TestClockTick = 20234l

    val cliParser = mock[CliParser]

    val solver = mock[ChessChallengeSolver]

    lazy val TestEnvironment = Environment(List("3", "3", "K2", "R1"), cliParser, solver)

    var initialRuntimeState = RuntimeState(clockTicks = Stream.iterate(0l)(_ + TestClockTick))

    def run[A](cmd: ReaderT[Free[IO, ?], Environment, A]): RuntimeState = {
      val (runtime, result) = cmd.run(TestEnvironment).foldMap(ConsoleToState or SystemToState).run(initialRuntimeState)
      runtime.copy(stdout = runtime.stdout.reverse, stderr = runtime.stderr.reverse)
    }

  }

}
