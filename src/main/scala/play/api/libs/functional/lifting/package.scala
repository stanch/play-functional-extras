package play.api.libs.functional

import scala.language.experimental.macros
import scala.language.higherKinds

package object lifting {
  import TupleLifting._

  implicit class LiftableTuple[A <: Product](tuple: A) {
    def liftAll[M[_]](implicit app: Applicative[M]): M[Any] = macro liftAllImpl[M]
  }
}
