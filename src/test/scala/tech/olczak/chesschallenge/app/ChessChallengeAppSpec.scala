package tech.olczak.chesschallenge.app

import org.mockito.Matchers.{any, eq => argEq}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import tech.olczak.chesschallenge.app.ChessChallengeApp.{IO, mainCmd}
import tech.olczak.chesschallenge.app.cli.{CliParser, ParseFailure}
import tech.olczak.chesschallenge.app.effect._
import tech.olczak.chesschallenge.chess._
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

    implicit def toPiecePosition(tuple: (Piece, (Int, Int))): Placement = Placement(tuple._1, Square(tuple._2._1, tuple._2._2))


    "solutions are found" should {
      "print the total number of unique arrangements" in new TestContext {
        //given
        when(cliParser.parse(any[List[String]])).thenReturn(\/-(ChessProblem(Board(3, 3), List(King -> 2, Rook -> 1))))
        when(solver.solve(any[Board], any[List[(Piece, Int)]])).thenReturn(
          List(
            Chessboard(Board(3, 3), Set(King ->(0, 0), King ->(0, 2), Rook ->(2, 1))),
            Chessboard(Board(3, 3), Set(King ->(0, 0), Rook ->(1, 2), King ->(2, 0))),
            Chessboard(Board(3, 3), Set(King ->(0, 2), Rook ->(1, 0), King ->(2, 2))),
            Chessboard(Board(3, 3), Set(Rook ->(0, 1), King ->(2, 0), King ->(2, 2)))
          )
        )
        //when
        run(mainCmd)
        //then
        stdout should contain("Found 4 solutions.")
      }
    }

  }

  trait TestContext {

    val cliParser = mock[CliParser]

    val solver = mock[ChessChallengeSolver]

    lazy val env = Environment(List("3", "3", "K2", "R1"), cliParser, solver)

    var buffer = Buffer(List.empty)

    def run[A](cmd: ReaderT[Free[IO, ?], Environment, A]): A = {
      val (buf, result) = cmd.run(env).foldMap(ConsoleToState or SystemToState).run(Buffer(List.empty))
      buffer = buf
      result
    }

    def stdout: List[String] = buffer.stdout.reverse

    def stderr: List[String] = buffer.stderr.reverse

    def errorCode: Int = buffer.exitCode.getOrElse(0)

  }

}
