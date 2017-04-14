name := "chess-challenge"

organization := "tech.olczak"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "Tim Tennant's repo" at "http://dl.bintray.com/timt/repo/"

resolvers += "bintray/non" at "http://dl.bintray.com/non/maven"

val scalaz = Seq (
  "org.scalaz" %% "scalaz-core" % "7.2.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.0"
)

val log = Seq(
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2" % Provided exclude("org.scala-lang", "scala-reflect"),
  "ch.qos.logback" % "logback-classic" % "1.1.2" % Test
)

val testLibs = Seq(
  "org.scalatest" %% "scalatest" % "2.2.2" % Test,
  "org.scalacheck" %% "scalacheck" % "1.11.6" % Test,
  "org.mockito" % "mockito-all" % "1.10.19" % Test
)

libraryDependencies ++= scalaz ++ testLibs ++ log

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }