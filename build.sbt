name := "play-functional-hlist"

organization := "org.needs"

version := "1.0.0"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.10.3"

autoCompilerPlugins := true

scalacOptions ++= Seq(
  "-feature"//,
  //"-Xlog-implicits"
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases"),
  Resolver.file("local-play", file("c:/projects/Play20/repository/local"))(Resolver.ivyStylePatterns)
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-datacommons" % "2.3-SNAPSHOT",
  "com.typesafe.play" %% "play-functional" % "2.3-SNAPSHOT",
  "com.chuusai" % "shapeless" % "2.0.0-M1" cross CrossVersion.full,
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "com.typesafe.play" %% "play-json" % "2.3-SNAPSHOT"
)