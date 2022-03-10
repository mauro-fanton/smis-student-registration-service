name := """SMISStudentRegistrationService"""
organization := "mauro.com.smis"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"
val akkaVersion = "2.6.18"

libraryDependencies ++= Seq(
  guice,
  "org.joda" % "joda-convert" % "2.2.1",
  "net.logstash.logback" % "logstash-logback-encoder" % "6.2",
  "io.lemonlabs" %% "scala-uri" % "1.5.1",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "org.typelevel" %% "cats-core" % "2.6.1",

  //Akka Persistence
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.0.5",
//  "com.lightbend.akka" %% "akka-projection-cassandra" % "1.2.3",
//  "be.wegenenverkeer" %% "akka-persistence-pg" % "0.14.0",
  "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "1.0.5" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "com.typesafe.play" %% "play-specs2" % "2.8.8" % Test
)

PlayKeys.devSettings := Seq("play.server.http.port" -> "9003")
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "mauro.com.smis.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "mauro.com.smis.binders._"
