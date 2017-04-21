package tech.olczak.chesschallenge.app

import java.util.concurrent.TimeUnit

import tech.olczak.chesschallenge.app.cli.ParseFailure
import tech.olczak.chesschallenge.app.effect.ConsoleIO._
import tech.olczak.chesschallenge.app.effect.SystemIO._
import tech.olczak.chesschallenge.app.effect.{ConsoleIO, SystemIO}
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.concurrent.duration.Duration
import scalaz.Scalaz._
import scalaz._

object ChessChallengeApp {

  type IO[A] = Coproduct[ConsoleIO, SystemIO, A]

  val mainCmd = ReaderT[Free[IO, ?], Environment, Unit] { env: Environment =>
    for {
      _ <- printLine[IO]("Hello, starting chess challenge app...")
      _ <- env.cliParser.parse(env.args) fold (handleParseFailure, solve(_).run(env))
    } yield ()
  }

  private def solve(chessConfig: ChessConfig) = ReaderT[Free[IO, ?], Environment, Unit] { env: Environment =>
    for {
      _                     <- printLine[IO](s"Solving chess challenge for ${chessConfig.board} and ${chessConfig.pieceGroups}")
      (solutions, duration) <- measure { env.solver.solve(chessConfig) }
      _                     <- printLine[IO](s"Found ${solutions.size} solutions.")
      _                     <- printLine[IO](s"Elapsed time: ${duration.toSeconds} sec and ${duration.toMillis % 1000} millis.")
      _                     <- solutions.traverseU(c => printLine[IO](c.shows))
    } yield ()
  }

  private def measure[A](block: => A) =
    for {
      start     <- getCurrentMillis[IO]
      result     = block
      end       <- getCurrentMillis[IO]
      duration   = Duration(end - start, TimeUnit.MILLISECONDS)
    } yield (result, duration)

  private def handleParseFailure(failure: ParseFailure) =
    for {
      _ <- printError[IO](s"Invalid arguments: ${failure.msg}")
      _ <- printError[IO]("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      _ <- exit[IO](1)
    } yield ()

}
