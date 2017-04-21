package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.app.effect.ConsoleIO

import scalaz.Free
import ConsoleIO._

object ChessChallengeApp {

  type Cmd[A] = Free[ConsoleIO, A]


  val mainCmd = printLine("Hello, starting chess challenge app...")


}
