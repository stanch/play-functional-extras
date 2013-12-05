package play.api.libs.functional

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.Context

object TupleLifting {
  def liftAllImpl[M[_]](c: Context)(app: c.Expr[Applicative[M]])(implicit weakTypeOfM: c.WeakTypeTag[M[_]]) = {
    import c.universe._
    val Apply(_, List(victim)) = c.prefix.tree
    val TypeRef(_, tup, args) = victim.tpe.widen
    if (!tup.name.toString.startsWith("Tuple")) {
      // TODO: more sane check for tuples?
      c.abort(c.enclosingPosition, s"$tup should be a tuple")
    }
    val builder = args.zipWithIndex.map { case (tp, i) ⇒
      val accessor = newTermName(s"_${i + 1}")

      // TODO: why <:< does not work? @xeno-by
      //if (tp <:< weakTypeOf[M[_]]) q"$victim.$accessor" else q"$app.pure($victim.$accessor)"

      if (tp.baseClasses.contains(weakTypeOf[M[_]].typeSymbol)) q"$victim.$accessor" else q"$app.pure($victim.$accessor)"
    } reduce { (x, y) ⇒
      // using and from play applicative builders
      q"$x and $y"
    }
    val res = q"{ import play.api.libs.functional.syntax._; $builder.tupled }"
    c.macroApplication.setType(c.typeCheck(res).tpe)
    c.Expr[M[Any]](res)
  }
}