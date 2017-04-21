package tech.olczak.chesschallenge.app.effect

import scala.language.higherKinds
import scalaz.{Free, Functor, Inject}

sealed trait ConsoleIO[A]
final case class PrintLine[A](msg: String, next: A) extends ConsoleIO[A]

object ConsoleIO {

  implicit val consoleFunctor: Functor[ConsoleIO] = new Functor[ConsoleIO] {
    def map[A, B](fa: ConsoleIO[A])(f: A => B): ConsoleIO[B] =
      fa match {
        case PrintLine(msg, next) => PrintLine(msg, f(next))
      }
  }

  // Smart constructors
  def printLine[F[_]: Functor](msg: String)(implicit I: Inject[ConsoleIO, F]): Free[F, Unit] =
    Inject.inject[F, ConsoleIO, Unit](PrintLine(msg, Free.point(())))

}
