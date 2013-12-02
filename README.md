### Applicative and functor extensions

This package provides two utilities (`import play.api.libs.json.applicative._` assumed):

* Mapping inside `Reads[F[A]]`, where `F` is a `Functor`:
  ```scala
  JsNumber(1).as(__.read[Option[Int]].fmap(_ + 42)) should equal (Some(43))
  ```

* Lifting `Reads[(A, M[B], M[C], D, M[E])]` (i.e. tuples of either `X` or `M[X]`) to `Reads[M[(A, B, C, D, E)]]`:
  ```scala
  def getById(id: Int): Future[String] = ...

  val reads: Reads[Future[(Int, String)]] = (
    (__ \ 'id).read[Int] and
    (__ \ 'otherId).read[Int].map(getById)
  ).tupled.liftAll[Future]
  ```
  
### Getting it

```scala
resolvers += "Stanch@bintray" at "http://dl.bintray.com/stanch/maven"

libraryDependencies += "org.needs" %% "play-json-applicative" % "1.0.0"
```
