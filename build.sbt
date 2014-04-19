name := "play-functional-extras"

organization := "org.resolvable"

version := "1.0.0"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.10.4"

autoCompilerPlugins := true

scalacOptions ++= Seq("-feature", "-deprecation")

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  compilerPlugin("org.scalamacros" % "paradise" % "2.0.0-M7" cross CrossVersion.full),
  "org.scalamacros" %% "quasiquotes" % "2.0.0-M7"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-functional" % "2.2.0",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)
