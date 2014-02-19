package play.api.libs.functional.hlist

import scala.language.higherKinds
import shapeless._
import play.api.libs.functional.Applicative
import shapeless.ops.hlist.RightFolder
import play.api.data.mapping.{From, Rule, Reader}

private[hlist] trait Lifting {
  object applicativeFolder extends Poly2 {
    implicit def caseApp[A, B <: HList, F[_]](implicit app: Applicative[F]) =
      at[F[A], F[B]] { (a, b) ⇒
        app.apply[A, A :: B](app.map[B, A ⇒ A :: B](b, x ⇒ y ⇒ y :: x), a)
      }
    implicit def casePure[A, B <: HList, F[_]](implicit app: Applicative[F], pure: A <:!< F[_]) =
      at[A, F[B]] { (a, b) ⇒
        app.apply[A, A :: B](app.map[B, A ⇒ A :: B](b, x ⇒ y ⇒ y :: x), app.pure(a))
      }
  }

  object applicativeFolder2 extends Poly2 {
    implicit def caseApp[A, B <: HList, I, F[_, _]](implicit app: Applicative[({ type λ[O] = F[I, O] })#λ]) =
      at[F[I, A], F[I, B]] { (a, b) ⇒
        app.apply[A, A :: B](app.map[B, A ⇒ A :: B](b, x ⇒ y ⇒ y :: x), a)
      }
    implicit def casePure[A, B <: HList, I, F[_, _]](implicit app: Applicative[({ type λ[O] = F[I, O] })#λ], pure: A <:!< F[I, _]) =
      at[A, F[I, B]] { (a, b) ⇒
        app.apply[A, A :: B](app.map[B, A ⇒ A :: B](b, x ⇒ y ⇒ y :: x), app.pure(a))
      }
  }

  implicit class ApplicativeLiftableHList[L <: HList](l: L) {
    def liftA[F[_]](implicit app: Applicative[F], folder: RightFolder[L, F[HNil], applicativeFolder.type]) =
      l.foldRight(app.pure(HNil: HNil))(applicativeFolder)

    def liftA[F[_, _], I](implicit app: Applicative[({ type λ[O] = F[I, O] })#λ], folder: RightFolder[L, F[I, HNil], applicativeFolder2.type], dummy: DummyImplicit) =
      l.foldRight(app.pure(HNil: HNil))(applicativeFolder2)
  }
}

private[hlist] trait Rules {
  def from[I] = new FromMaker[I]
  class FromMaker[I] {
    def apply[L <: HList, O](l: Reader[I] ⇒ L)(implicit folder: RightFolder.Aux[L, Rule[I, HNil], applicativeFolder2.type, Rule[I, O]]) =
      From[I](__ ⇒ l(__).liftA[Rule, I])
  }
}
