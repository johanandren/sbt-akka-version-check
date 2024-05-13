version := "0.1"
scalaVersion := "2.13.14"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

// direct dependency mismatch
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-discovery" % "2.9.2",
  "com.lightbend.akka.management" %% "akka-management" % "1.5.1",
  "com.lightbend.akka.discovery" %% "akka-discovery-aws-api" % "1.5.0"
)

TaskKey[Unit]("check") := {
  val lastLog: File = BuiltinCommands.lastLogFile(state.value).get
  val last: String  = IO.read(lastLog)
  if (!last.contains("Non matching Akka Management module versions"))
    sys.error("expected mention of non matching Akka Management module versions")
}
