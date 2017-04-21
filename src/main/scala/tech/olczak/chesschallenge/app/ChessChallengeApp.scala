package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.app.cli.ParseFailure
import tech.olczak.chesschallenge.app.effect.ConsoleIO._
import tech.olczak.chesschallenge.app.effect.SystemIO._
import tech.olczak.chesschallenge.app.effect.{ConsoleIO, SystemIO}

import scalaz._

object ChessChallengeApp {

  type IO[A] = Coproduct[ConsoleIO, SystemIO, A]

  val mainCmd = ReaderT[Free[IO, ?], Environment, Unit] { env: Environment =>
    for {
      _ <- printLine[IO]("Hello, starting chess challenge app...")
      _ <- env.cliParser.parse(env.args) match {
        case -\/(failure) =>
          handleParseFailure(failure)
        case \/-(ChessProblem(board, pieces)) =>
          for {
            _         <- printLine[IO](s"Looking solutions for $board and $pieces")
            solutions =  env.solver.solve(board, pieces)
            _         <- printLine[IO](s"Found ${solutions.size} solutions.")
          } yield ()
       }
    } yield ()
  }

  private def solveChessProblem(problem: ChessProblem) = ???

  private def handleParseFailure(failure: ParseFailure) =
    for {
      _ <- printError[IO](s"Invalid arguments: ${failure.msg}")
      _ <- printError[IO]("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      _ <- exit[IO](1)
    } yield ()


}
