version := "0.1"
scalaVersion := "2.13.14"

// direct dependency mismatch
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-discovery" % "2.6.14",
  "com.lightbend.akka.management" %% "akka-management" % "1.1.4",
  "com.lightbend.akka.discovery" %% "akka-discovery-aws-api" % "1.1.3"
)

TaskKey[Unit]("check") := {
  val lastLog: File = BuiltinCommands.lastLogFile(state.value).get
  val last: String  = IO.read(lastLog)
  if (!last.contains("Non matching Akka Management module versions"))
    sys.error("expected mention of non matching Akka Management module versions")
}
