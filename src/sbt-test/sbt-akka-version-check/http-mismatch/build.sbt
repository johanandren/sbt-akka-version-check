version := "0.1"
scalaVersion := "2.12.1"

// Akka http 10.1.x requires at least Akka 2.5
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.20",
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
)

TaskKey[Unit]("check") := {
  val lastLog: File = BuiltinCommands.lastLogFile(state.value).get
  val last: String  = IO.read(lastLog)
  if (!last.contains("Akka HTTP requires Akka 2.5 or later but was 2.4.20"))
    sys.error("expected mention of non matching Akka and Akka HTTP versions")
}
