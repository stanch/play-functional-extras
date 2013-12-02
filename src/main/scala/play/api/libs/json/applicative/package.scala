package play.api.libs.json

import scala.language.higherKinds
import scala.language.experimental.macros
import play.api.libs.functional.{Functor, Applicative}

package object applicative {
  import Lifting._

  implicit class LiftingReads[A](reads: Reads[A]) {
    /** Convert `Reads[(P, M[Q], M[R], S, M[T])]` to `Reads[M[(P, Q, R, S, T)]]` for Applicative M */
    def liftAll[M[_]](implicit app: Applicative[M]): Reads[M[Any]] = macro liftAllImpl[M]
  }

  implicit class MappingReads[M[_]: Functor, A](reads: Reads[M[A]]) {
    /** Map a functor M inside Reads */
    def fmap[B](f: A ⇒ B): Reads[M[B]] = reads.map(v ⇒ implicitly[Functor[M]].fmap(v, f))
  }
}
