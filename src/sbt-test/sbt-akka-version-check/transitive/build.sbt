version := "0.1"
scalaVersion := "2.12.19"

// transitive dependency mismatch (Couchbase 1.0 pulls in Akka 2.5.21 modules)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.26",
  "com.lightbend.akka" %% "akka-persistence-couchbase" % "1.0",
)