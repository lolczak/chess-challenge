package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.app.effect._

import scala.language.higherKinds
import scalaz._

object ConsoleToState extends (ConsoleIO ~> TestState) {

  override def apply[A](in: ConsoleIO[A]): State[Buffer, A] =
    in match {
      case PrintLine(msg, next)  => State[Buffer, A](buf => (buf.copy(stdout = msg :: buf.stdout), next))
      case PrintError(msg, next) => State[Buffer, A](buf => (buf.copy(stderr = msg :: buf.stderr), next))
    }


}

object SystemToState extends (SystemIO ~> TestState) {

  override def apply[A](in: SystemIO[A]): State[Buffer, A] =
    in match {
      case Exit(status, next)     => State[Buffer, A](buf => (buf.copy(exitCode = Some(status)), next))
      case GetCurrentMillis(next) => State[Buffer, A](buf => (buf.copy(millis = buf.millis.tail), next(buf.millis.head)))
    }

}

case class Buffer(stdout: List[String] = List.empty,
                  stderr: List[String] = List.empty,
                  millis: Stream[Long] = Stream.empty,
                  exitCode: Option[Int] = None
                 )