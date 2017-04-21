package tech.olczak.chesschallenge.app

import scala.language.higherKinds
import scalaz._

package object effect {

  implicit class NaturalTransformationOr[F[_], G[_]](self: F ~> G) {

    import scalaz.{-\/, \/-}

    def or[H[_]](f: H ~> G): Coproduct[F, H, ?] ~> G =
      new (Coproduct[F, H, ?] ~> G) {
        def apply[A](c: Coproduct[F, H, A]): G[A] = c.run match {
          case -\/(fa) => self(fa)
          case \/-(ha) => f(ha)
        }
      }
  }

  /*
      Free monad doesn't have filter function. This function is needed for pattern matching in for comprehension.
   */
  implicit class FreeFilter[S[_], A](free: Free[S, A]) {

    def filter(f: A => Boolean): Free[S, A] =
      free flatMap { item =>
        if (f(item)) free
        else throw new RuntimeException
      }

  }

}
