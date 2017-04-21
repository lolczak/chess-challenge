package tech.olczak.chesschallenge.app.effect

import scala.language.higherKinds
import scalaz.{Id, _}

sealed trait ConsoleIO[A]
final case class PrintLine[A](msg: String, next: A) extends ConsoleIO[A]
final case class PrintError[A](msg: String, next: A) extends ConsoleIO[A]

object ConsoleIO {

  implicit val consoleFunctor: Functor[ConsoleIO] = new Functor[ConsoleIO] {
    def map[A, B](fa: ConsoleIO[A])(f: A => B): ConsoleIO[B] =
      fa match {
        case PrintLine(msg, next)  => PrintLine(msg, f(next))
        case PrintError(msg, next) => PrintError(msg, f(next))
      }
  }

  // Smart constructors
  def printLine[F[_] : Functor](msg: String)(implicit I: Inject[ConsoleIO, F]): Free[F, Unit] =
    Inject.inject[F, ConsoleIO, Unit](PrintLine(msg, Free.point(())))

  def printError[F[_] : Functor](msg: String)(implicit I: Inject[ConsoleIO, F]): Free[F, Unit] =
    Inject.inject[F, ConsoleIO, Unit](PrintError(msg, Free.point(())))

}

object RealConsole extends (ConsoleIO ~> Id.Id) {
  import Id._
  import scalaz.syntax.monad._

  def apply[A](in: ConsoleIO[A]): Id[A] =
    in match {
      case PrintLine(msg, next) =>
        println(msg)
        next.point[Id]

      case PrintError(msg, next) =>
        System.err.println(msg)
        next.point[Id]
    }
}