cancelable in Global := true

name := "bacteria"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,   

  //we may need this later
  //so I'll just leave it here
  // [technically we need akka-testkit but it needs scalatest anyway]
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  )
