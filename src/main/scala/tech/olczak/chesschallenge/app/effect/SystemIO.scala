package tech.olczak.chesschallenge.app.effect

import scala.language.higherKinds
import scalaz.{Free, Functor, Inject}

sealed trait SystemIO[A]
final case class Exit[A](status: Int, next: A) extends SystemIO[A]

object SystemIO {

  implicit val systemFunctor: Functor[SystemIO] = new Functor[SystemIO] {
    def map[A, B](fa: SystemIO[A])(f: A => B): SystemIO[B] =
      fa match {
        case Exit(status, next) => Exit(status, f(next))
      }
  }

  // Smart constructors
  def exit[F[_] : Functor](status: Int)(implicit I: Inject[SystemIO, F]): Free[F, Unit] =
    Inject.inject[F, SystemIO, Unit](Exit(status, Free.point(())))

}