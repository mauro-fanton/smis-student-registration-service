name := """SMISStudentRegistrationService"""
organization := "mauro.com.smis"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.15"
val akkaVersion = "2.6.19"

versionScheme := Some("early-semver")

libraryDependencies ++= Seq(
  guice,
 // "org.joda" % "joda-convert" % "2.2.1",
  "net.logstash.logback" % "logstash-logback-encoder" % "7.0.1",
  "io.lemonlabs" %% "scala-uri" % "4.0.2",
  "net.codingwell" %% "scala-guice" % "5.0.2",
  //"org.typelevel" %% "cats-core" % "2.6.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.19",
  "ch.qos.logback" % "logback-classic" % "1.2.11",

  //Akka Persistence
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.0.5",
  "com.lightbend.akka" %% "akka-projection-cassandra" % "1.2.3",
  "com.lightbend.akka" %% "akka-projection-eventsourced" % "1.2.3",
  "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-typed" % akkaVersion,
  "io.getquill" %% "quill-cassandra" % "3.17.0-RC2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.3",
  "com.datastax.cassandra"    % "cassandra-driver-core"   % "3.1.1",
  "com.typesafe.akka" %% "akka-persistence-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "1.0.5" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "com.typesafe.play" %% "play-specs2" % "2.8.15" % Test,
  "com.lightbend.akka" %% "akka-projection-testkit" % "1.2.4" % Test
)

PlayKeys.devSettings := Seq("play.server.http.port" -> "9003")

