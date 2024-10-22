version := "0.1"
scalaVersion := "3.3.4"

// direct dependency mismatch
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.19",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.18"
)

TaskKey[Unit]("check") := {
  val lastLog: File = BuiltinCommands.lastLogFile(state.value).get
  val last: String  = IO.read(lastLog)
  if (!last.contains("Non matching Akka module versions"))
    sys.error("expected mention of non matching Akka module versions")
}
