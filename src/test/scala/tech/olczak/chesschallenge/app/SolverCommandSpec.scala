package tech.olczak.chesschallenge.app

import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import tech.olczak.chesschallenge.ChessObjectMother._
import tech.olczak.chesschallenge.app.Commands.{Effect, solverCmd}
import tech.olczak.chesschallenge.app.cli.{CliParser, ParseFailure}
import tech.olczak.chesschallenge.app.constant.Constants._
import tech.olczak.chesschallenge.app.constant.Messages._
import tech.olczak.chesschallenge.app.effect._
import tech.olczak.chesschallenge.app.interpreter.{ConsoleToState, RuntimeState, SystemToState}
import tech.olczak.chesschallenge.solver.ChessChallengeSolver

import scalaz._

class SolverCommandSpec extends WordSpec with Matchers with MockitoSugar {

  "A solver command" when {

    "starting" should {
      "print greeting" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        val runtime = run(solverCmd)
        //then
        runtime.stdout should contain(GreetingMsg)
      }
    }

    "command line arguments are invalid" should {
      "fail with error code equal 1" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        val runtime = run(solverCmd)
        //then
        runtime.maybeExitCode shouldBe Some(FailureExitCode)
      }

      "print error message" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(-\/(ParseFailure("rank count missing")))
        //when
        val runtime = run(solverCmd)
        //then
        runtime.stderr should contain (InvalidArgumentsMsg format "rank count missing")
        runtime.stderr should contain (UsageMsg)
      }
    }

    "solutions are found" should {

      "print the total number of unique arrangements" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        val runtime = run(solverCmd)
        //then
        runtime.stdout should contain (FoundSolutionsMsg format 4)
      }

      "print the time the solver took to find all solutions" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        val runtime = run(solverCmd)
        //then
        runtime.stdout should contain (ElapsedTimeMsg format (ExpectedSeconds, ExpectedMillis))
      }

      "list all solutions to the console" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(TinyChessConfig))
        when(solver.solve(TinyChessConfig)).thenReturn(TinyConfigSolutions)
        //when
        val runtime = run(solverCmd)
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

    val ExpectedSeconds = TestClockTick / MillisInSecond

    val ExpectedMillis = TestClockTick % MillisInSecond

    val cliParser = mock[CliParser]

    val solver = mock[ChessChallengeSolver]

    lazy val TestEnvironment = Environment(List("3", "3", "K2", "R1"), cliParser, solver)

    var initialRuntimeState = RuntimeState(clockTicks = Stream.iterate(0l)(_ + TestClockTick))

    def run[A](cmd: ReaderT[Free[Effect, ?], Environment, A]): RuntimeState = {
      val (runtime, result) = cmd.run(TestEnvironment).foldMap(ConsoleToState or SystemToState).run(initialRuntimeState)
      runtime.copy(stdout = runtime.stdout.reverse, stderr = runtime.stderr.reverse)
    }

  }

}
