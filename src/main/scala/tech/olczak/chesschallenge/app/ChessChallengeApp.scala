package tech.olczak.chesschallenge.app

import java.util.concurrent.TimeUnit

import tech.olczak.chesschallenge.app.cli.ParseFailure
import tech.olczak.chesschallenge.app.effect.ConsoleIO._
import tech.olczak.chesschallenge.app.effect.SystemIO._
import tech.olczak.chesschallenge.app.effect.{ConsoleIO, SystemIO}
import tech.olczak.chesschallenge.chess.Chessboard
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.concurrent.duration.Duration
import scalaz.Scalaz._
import scalaz._

object ChessChallengeApp {

  type Effect[A] = Coproduct[ConsoleIO, SystemIO, A]

  type Action[A] = ReaderT[Free[Effect, ?], Environment, A]

  val solverAction = ReaderT[Free[Effect, ?], Environment, Unit] { env: Environment =>
    for {
      _ <- printLine[Effect]("Hello, starting chess challenge app...")
      _ <- env.cliParser.parse(env.args) fold (handleParseFailure, solve(_).run(env))
    } yield ()
  }

  private def solve(chessConfig: ChessConfig) = ReaderT[Free[Effect, ?], Environment, Unit] { env: Environment =>
    for {
      _                     <- printLine[Effect](s"Solving chess challenge for ${chessConfig.board} and ${chessConfig.pieceGroups}")
      (solutions, duration) <- measure { env.solver.solve(chessConfig) }
      _                     <- printLine[Effect](s"Found ${solutions.size} solutions.")
      _                     <- printLine[Effect](s"Elapsed time: ${duration.toSeconds} sec and ${duration.toMillis % 1000} millis.")
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
      _ <- printError[Effect](s"Invalid arguments: ${failure.msg}")
      _ <- printError[Effect]("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      _ <- exit[Effect](1)
    } yield ()

}
