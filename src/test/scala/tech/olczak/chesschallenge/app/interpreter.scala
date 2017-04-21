package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.app.effect._

import scala.language.higherKinds
import scalaz._

object interpreter {

  type TestState[A] = State[RuntimeState, A]

  object ConsoleToState extends (ConsoleIO ~> TestState) {

    override def apply[A](in: ConsoleIO[A]): State[RuntimeState, A] =
      in match {
        case PrintLine(msg, next) =>
          State[RuntimeState, A](rs => (rs.copy(stdout = msg :: rs.stdout), next))

        case PrintError(msg, next) =>
          State[RuntimeState, A](rs => (rs.copy(stderr = msg :: rs.stderr), next))
      }


  }

  object SystemToState extends (SystemIO ~> TestState) {

    override def apply[A](in: SystemIO[A]): State[RuntimeState, A] =
      in match {
        case Exit(status, next) =>
          State[RuntimeState, A](rs => (rs.copy(maybeExitCode = Some(status)), next))

        case GetCurrentMillis(next) =>
          State[RuntimeState, A](rs => (rs.copy(clockTicks = rs.clockTicks.tail), next(rs.clockTicks.head)))
      }

  }

  case class RuntimeState(stdout: List[String] = List.empty,
                          stderr: List[String] = List.empty,
                          clockTicks: Stream[Long] = Stream.empty,
                          maybeExitCode: Option[Int] = None)

}