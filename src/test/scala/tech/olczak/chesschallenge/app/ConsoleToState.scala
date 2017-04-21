package tech.olczak.chesschallenge.app
import scala.language.higherKinds
import tech.olczak.chesschallenge.app.effect.{PrintLine, ConsoleIO}

import scalaz._

object ConsoleToState extends (ConsoleIO ~> State[Buffer, ?]) {

  override def apply[A](in: ConsoleIO[A]): State[Buffer, A] = {
    in match {
      case PrintLine(msg, next) => State[Buffer, A](buf => (buf.copy(msg :: buf.stdout), next))
    }

  }
}

case class Buffer(stdout: List[String])