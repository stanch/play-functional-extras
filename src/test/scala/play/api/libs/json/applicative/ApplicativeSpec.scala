package play.api.libs.json.applicative

import org.scalatest.{Matchers, FlatSpec}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.functional.Applicative
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ApplicativeSpec extends FlatSpec with Matchers {
  "Reads of functors" should "provide an fmap" in {
    JsNumber(1).as(__.read[Option[Int]].fmap(_ + 42)) should equal (Some(43))
  }

  "Reads of pure values" should "be convertible to Reads of applicatives" in {
    implicit object futureApplicative extends Applicative[Future] {
      def pure[A](a: A) = Future.successful(a)
      def map[A, B](m: Future[A], f: A ⇒ B) = ??? // does not matter here
      def apply[A, B](mf: Future[A ⇒ B], ma: Future[A]) = ??? // does not matter here
    }
    JsNumber(1).as[Future[Int]] foreach (_ should equal (1))
  }

  "Reads of tuples" should "allow lifting to applicative context" in {
    val read: Reads[Option[(Int, String)]] =
      ((__ \ 'id).read[Int] and (__ \ 'title).read[Option[String]]).tupled.liftAll[Option]
    Json.obj("id" → 2, "title" → "3").as(read) should equal (Some(2, "3"))
  }
}
