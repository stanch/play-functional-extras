name := "play-functional-extras"

organization := "org.resolvable"

version := "1.0.2"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.1")

autoCompilerPlugins := true

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  compilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)
)

libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, 10)) ⇒
    Seq("org.scalamacros" %% "quasiquotes" % "2.0.0")
  case _ ⇒
    Seq()
})

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-functional" % "2.3.0",
  "org.scalatest" %% "scalatest" % "2.1.5" % "test"
)
