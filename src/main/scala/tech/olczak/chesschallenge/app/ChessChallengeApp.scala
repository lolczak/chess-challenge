package tech.olczak.chesschallenge.app

import java.util.concurrent.TimeUnit

import tech.olczak.chesschallenge.app.cli.ParseFailure
import tech.olczak.chesschallenge.app.effect.ConsoleIO._
import tech.olczak.chesschallenge.app.effect.SystemIO._
import tech.olczak.chesschallenge.app.effect.{ConsoleIO, SystemIO}

import scala.concurrent.duration.Duration
import scalaz._
import Scalaz._

object ChessChallengeApp {

  type IO[A] = Coproduct[ConsoleIO, SystemIO, A]

  val mainCmd = ReaderT[Free[IO, ?], Environment, Unit] { env: Environment =>
    for {
      _ <- printLine[IO]("Hello, starting chess challenge app...")
      _ <- env.cliParser.parse(env.args) match {
        case -\/(failure) =>
          handleParseFailure(failure)
        case \/-(chessConfig) =>
          for {
            _         <- printLine[IO](s"Looking solutions for ${chessConfig.board} and ${chessConfig.pieceGroups}")
            start     <- getCurrentMillis[IO]
            solutions =  env.solver.solve(chessConfig)
            end       <- getCurrentMillis[IO]
            duration   = Duration(end - start, TimeUnit.MILLISECONDS)
            _         <- printLine[IO](s"Found ${solutions.size} solutions.")
            _         <- printLine[IO](s"Elapsed time: ${duration.toSeconds} sec and ${duration.toMillis % 1000} millis.")
            _         <- solutions.traverseU(c => printLine[IO](c.shows))
          } yield ()
       }
    } yield ()
  }

  private def handleParseFailure(failure: ParseFailure) =
    for {
      _ <- printError[IO](s"Invalid arguments: ${failure.msg}")
      _ <- printError[IO]("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      _ <- exit[IO](1)
    } yield ()


}
