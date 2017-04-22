package tech.olczak.chesschallenge.app.effect

import scala.language.higherKinds
import scalaz._
import scalaz.effect.IO

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

  def printLine[F[_] : Functor, A](item: A)(implicit I: Inject[ConsoleIO, F], S: Show[A]): Free[F, Unit] =
    Inject.inject[F, ConsoleIO, Unit](PrintLine(S shows item, Free.point(())))

  def printError[F[_] : Functor](msg: String)(implicit I: Inject[ConsoleIO, F]): Free[F, Unit] =
    Inject.inject[F, ConsoleIO, Unit](PrintError(msg, Free.point(())))

}

object RealConsole extends (ConsoleIO ~> IO) {

  def apply[A](in: ConsoleIO[A]): IO[A] =
    in match {
      case PrintLine(msg, next) =>
        IO.putStrLn(msg) map (_ => next)

      case PrintError(msg, next) =>
        IO { System.err.println(msg) } map (_ => next)
    }

}