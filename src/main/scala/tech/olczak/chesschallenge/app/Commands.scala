package tech.olczak.chesschallenge.app

import java.util.concurrent.TimeUnit

import tech.olczak.chesschallenge.app.cli.ParseFailure
import tech.olczak.chesschallenge.app.constant.Constants._
import tech.olczak.chesschallenge.app.constant.Messages._
import tech.olczak.chesschallenge.app.effect.ConsoleIO._
import tech.olczak.chesschallenge.app.effect.SystemIO._
import tech.olczak.chesschallenge.app.effect.{ConsoleIO, SystemIO}
import tech.olczak.chesschallenge.chess.Chessboard
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.concurrent.duration.Duration
import scalaz.Scalaz._
import scalaz._

object Commands {

  type Effect[A] = Coproduct[ConsoleIO, SystemIO, A]

  type Cmd[A] = ReaderT[Free[Effect, ?], Environment, A]

  val solverCmd = ReaderT[Free[Effect, ?], Environment, Unit] { env: Environment =>
    for {
      _ <- printLine[Effect](GreetingMsg)
      _ <- env.cliParser.parse(env.args) fold (handleParseFailure, solve(_).run(env))
    } yield ()
  }

  private def solve(chessConfig: ChessConfig) = ReaderT[Free[Effect, ?], Environment, Unit] { env: Environment =>
    for {
      _                     <- printLine[Effect](SolverStartMsg format (chessConfig.board, chessConfig.pieceGroups))
      (solutions, duration) <- measure { env.solver.solve(chessConfig) }
      _                     <- printLine[Effect](FoundSolutionsMsg format solutions.size)
      _                     <- printLine[Effect](ElapsedTimeMsg format (duration.toSeconds, duration.toMillis % MillisInSecond))
      _                     <- solutions traverseU printLine[Effect, Chessboard]
    } yield ()
  }

  private def measure[A](block: => A) =
    for {
      start     <- getCurrentMillis[Effect]
      result     = block
      end       <- getCurrentMillis[Effect]
      duration   = Duration(end - start, TimeUnit.MILLISECONDS)
    } yield (result, duration)

  private def handleParseFailure(failure: ParseFailure) =
    for {
      _ <- printError[Effect](InvalidArgumentsMsg format failure.msg)
      _ <- printError[Effect](UsageMsg)
      _ <- exit[Effect](FailureExitCode)
    } yield ()

}
