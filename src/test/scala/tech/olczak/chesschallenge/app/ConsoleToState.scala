package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.app.effect.{ConsoleIO, PrintLine}

import scala.language.higherKinds
import scalaz._

object ConsoleToState extends (ConsoleIO ~> State[Buffer, ?]) {

  override def apply[A](in: ConsoleIO[A]): State[Buffer, A] = {
    in match {
      case PrintLine(msg, next) => State[Buffer, A](buf => (buf.copy(msg :: buf.stdout), next))
    }

  }
}

case class Buffer(stdout: List[String])