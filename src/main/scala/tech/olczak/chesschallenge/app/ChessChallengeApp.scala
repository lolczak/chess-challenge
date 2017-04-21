package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.app.effect.ConsoleIO._
import tech.olczak.chesschallenge.app.effect.SystemIO._
import tech.olczak.chesschallenge.app.effect.{ConsoleIO, SystemIO}

import scalaz._

object ChessChallengeApp {

  type IO[A] = Coproduct[ConsoleIO, SystemIO, A]

  val mainCmd = ReaderT[Free[IO, ?], Environment, Unit] { env: Environment =>
    for {
      _ <- printLine[IO]("Hello, starting chess challenge app...")
      _ <- printError[IO]("Invalid arguments.")
      _ <- printError[IO]("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      _ <- exit[IO](1)
    } yield ()
  }

}
