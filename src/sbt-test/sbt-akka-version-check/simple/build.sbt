version := "0.1"
scalaVersion := "2.12.19"

// direct dependency mismatch
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.26",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.23",
)