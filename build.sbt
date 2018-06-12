cancelable in Global := true

name := "bacteria"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,

  //testow co prawda nie ma ale kopiowalem to skads,
  //a po co potem znowu szukac
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  )

