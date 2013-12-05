### Lifting

This package provides a very straightforward lifting operation on tuples:

```scala
import play.api.libs.functional.syntax._
import play.api.libs.functional.lifting._

"Lifting" should "work for pure tuples" in {
  (1, "foo").liftAll[Option] should equal (Some((1, "foo")))
  (1, "foo", 3.14).liftAll[Option] should equal (Some((1, "foo", 3.14)))
}

it should "work for tuples of mixed pure and in-context values" in {
  (Option(1), 2).liftAll[Option] should equal (Some((1, 2)))
  ("bar", Option(2)).liftAll[Option] should equal (Some(("bar", 2)))
  (1, Option(2), 3.5).liftAll[Option] should equal (Some((1, 2, 3.5)))
}

it should "work for tuples of in-context values" in {
  (Option(1), Option("baz")).liftAll[Option] should equal (Some((1, "baz")))
  (Option(1), Option(2), Option(3), Option(4)).liftAll[Option] should equal (Some((1, 2, 3, 4)))
}

it should "work when nested" in {
  (Option(1), (Option(2), 3).liftAll[Option]).liftAll[Option] should equal (Some((1, (2, 3))))
}
```
  
### Getting it

```scala
resolvers += "Stanch@bintray" at "http://dl.bintray.com/stanch/maven"

libraryDependencies += "org.needs" %% "play-functional-extras" % "1.0.0"
```
