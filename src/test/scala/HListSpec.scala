import org.scalatest.{ShouldMatchers, FlatSpec}
import play.api.libs.functional.hlist._
import play.api.libs.functional.syntax._
import shapeless._

case class A(foo: Int, bar: Int, baz: Int)

class HListSpec extends FlatSpec with ShouldMatchers {
  "Lifting" should "work for hlists of pure values" in {
    (1 :: "foo" :: HNil).liftA[Option] should equal (Some(1 :: "foo" :: HNil))
    (1 :: "foo" ::  3.14 :: HNil).liftA[Option] should equal (Some(1 :: "foo" :: 3.14 :: HNil))
  }

  it should "work for hlists of mixed pure and in-context values" in {
    (Option(1) :: 2 :: HNil).liftA[Option] should equal (Some(1 :: 2 :: HNil))
    ("bar" :: Option(2) :: HNil).liftA[Option] should equal (Some("bar" :: 2 :: HNil))
    (1 :: Option(2) :: 3.5 :: HNil).liftA[Option] should equal (Some(1 :: 2 :: 3.5 :: HNil))
  }

  it should "work for hlists of in-context values" in {
    (Option(1) :: Option("baz") :: HNil).liftA[Option] should equal (Some(1 :: "baz" :: HNil))
    (Option(1) :: Option(2) :: Option(3) :: Option(4) :: HNil).liftA[Option] should equal (Some(1 :: 2 :: 3 :: 4 :: HNil))
  }

  it should "work when nested" in {
    (Option(1) :: (Option(2) :: 3 :: HNil).liftA[Option] :: HNil).liftA[Option] should equal (Some(1 :: (2 :: 3 :: HNil) :: HNil))
  }

  "Rule support" should "work for lifting" in {
    import play.api.libs.json.{ Json, JsValue }
    import play.api.data.mapping.json.Rules._
    import play.api.data.mapping.Success

    val rule = from[JsValue] { __ ⇒
      (__ \ "foo").read[Int] ::
      (__ \ "bar").read[Int] ::
      3 ::
      HNil
    }.fmap(Generic[A].from)

    rule.validate(Json.parse("""{"foo": 3, "bar": 4}""")) should equal (Success(A(3, 4, 3)))
  }

  "Rule support" should "work with nested applicatives" in {
    import play.api.libs.json.{ Json, JsValue }
    import play.api.data.mapping.json.Rules._
    import play.api.data.mapping.{ Rule, From, Success }

    val rule = From[JsValue] { __ ⇒
      ((__ \ "foo").read[Option[Int]] ::
      (__ \ "bar").read[Int] ::
      3 ::
      HNil).liftA[Rule, JsValue].fmap(_.liftA[Option].map(Generic[A].from))
    }

    rule.validate(Json.parse("""{"foo": 3, "bar": 4}""")) should equal (Success(Some(A(3, 4, 3))))
  }
}
