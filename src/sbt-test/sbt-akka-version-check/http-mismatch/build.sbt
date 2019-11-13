version := "0.1"
scalaVersion := "2.12.1"

// Akka http 10.1.x requires at least Akka 2.5
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.26",
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
)