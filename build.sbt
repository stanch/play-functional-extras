name := "play-json-applicative"

organization := "org.needs"

version := "1.0.0"

scalaVersion := "2.10.3"

autoCompilerPlugins := true

scalacOptions ++= Seq(
  "-feature"//,
  //"-Ydebug"
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  compilerPlugin("org.scala-lang.plugins" % "macro-paradise" % "2.0.0-SNAPSHOT" cross CrossVersion.full)
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.2.0",
  "org.scalatest" %% "scalatest" % "2.0" % "test"
)