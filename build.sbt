name := """sbt-akka-version-check"""
organization := "com.markatta"
homepage := Some(url("https://github.com/johanandren/sbt-akka-version-check"))
licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")
developers += Developer(
  "johanandren",
  "Johan Andrén",
  "",
  url("https://github.com/johanandren")
)


console / initialCommands := """import com.markatta.akka.sbtvc._"""

enablePlugins(SbtPlugin)

scalaVersion := "2.12.20"
crossScalaVersions += "3.7.3"

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.9.7" // set minimum sbt version
    case _      => "2.0.0-RC4"
  }
}

// set up 'scripted; sbt plugin for testing sbt plugins
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)


// Customise sbt-dynver's behaviour to make it work with tags which aren't v-prefixed
ThisBuild / dynverVTagPrefix := true

// Sanity-check: assert that version comes from a tag (e.g. not a too-shallow clone)
// https://github.com/dwijnand/sbt-dynver/#sanity-checking-the-version
Global / onLoad := (Global / onLoad).value.andThen { s =>
  dynverAssertTagVersion.value
  s
}
