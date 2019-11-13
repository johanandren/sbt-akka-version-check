package com.lightbend.akka.sbtvc

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

case class AkkaVersionReport(akkaVersion: Option[VersionNumber], akkaHttpVersion: Option[VersionNumber])

object AkkaVersionCheckPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = JvmPlugin

  object autoImport {
    val checkAkkaModuleVersions = taskKey[AkkaVersionReport]("Check that all Akka modules have the same version")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    checkAkkaModuleVersions := checkModuleVersions(update.value.allModules, streams.value.log)
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()

  private val coreModules = Set(
    "akka" ,"akka-actor" ,"akka-actor-testkit-typed" ,"akka-actor-tests", "akka-actor-typed" ,"akka-actor-typed-tests",
    "akka-cluster", "akka-cluster-metrics", "akka-cluster-sharding", "akka-cluster-sharding-typed", "akka-cluster-tools",
    "akka-cluster-typed", "akka-coordination", "akka-discovery", "akka-distributed-data", "akka-multi-node-testkit" ,
    "akka-osgi", "akka-persistence", "akka-persistence-query", "akka-persistence-shared", "akka-persistence-tck" ,
    "akka-persistence-typed", "akka-protobuf", "akka-protobuf-v3", "akka-remote", "akka-serialization-jackson",
    "akka-slf4j", "akka-stream", "akka-stream-testkit","akka-stream-typed", "akka-testkit"
  )
  private val akkaHttpModules = Set(
    "akka-http", "akka-http-caching", "akka-http-core", "akka-http-jackson", "akka-http-marshallers-java",
    "akka-http-marshallers-scala", "akka-http-root", "akka-http-spray-json", "akka-http-testkit", "akka-http-xml",
    "akka-http2-support", "akka-parsing")


  private sealed trait Group
  private case object Akka extends Group
  private case object AkkaHttp extends Group
  private case object Others extends Group

  def checkModuleVersions(allModules: Seq[ModuleID], log: Logger): AkkaVersionReport = {
    log.debug("Checking Akka module versions")
    val grouped = allModules.groupBy(m =>
      if (m.organization == "com.typesafe.akka") {
        val nameWithoutScalaV = m.name.dropRight(5)
        if (coreModules(nameWithoutScalaV)) Akka
        else if (akkaHttpModules(nameWithoutScalaV)) AkkaHttp
        else Others
      }
    )
    val akkaVersion = grouped.get(Akka)
      .flatMap(verifyVersions("Akka", _))
      .map(VersionNumber.apply)
    val akkaHttpVersion = grouped.get(AkkaHttp)
      .flatMap(verifyVersions("Akka HTTP", _)
      .map(VersionNumber.apply))

    (akkaVersion, akkaHttpVersion) match {
      case (Some(akkaV), Some(akkaHttpV)) =>
        verifyAkkaHttpAkkaRequirement(akkaV, akkaHttpV)
      case _ => // whatever
    }
    // FIXME is it useful to verify more inter-project dependencies Akka vs Akka Persistence Cassandra etc.
    AkkaVersionReport(akkaVersion, akkaHttpVersion)
  }

  private def verifyVersions(project: String, modules: Seq[ModuleID]): Option[String] =
    modules.foldLeft(None: Option[String]) { (prev, module) =>
      prev match {
        case Some(version) =>
          if (module.revision != version) {
            // FIXME find out what pulled it in if transitive and say that as well in text
            // (do we need the depgraph plugin for that?)
            throw new MessageOnlyException(
              s"""| Non matching $project module versions, previously seen version $version,
                  | but module ${module.name} has version ${module.revision}.
            """.stripMargin)
          } else Some(version)
        case None => Some(module.revision)
      }
    }



  private def verifyAkkaHttpAkkaRequirement(akkaHttpVersion: VersionNumber, akkaVersion: VersionNumber): Unit = {
    if (akkaHttpVersion.matchesSemVer(SemanticSelector("10.1")) &&
      akkaVersion.matchesSemVer(SemanticSelector("<2.5"))) {
      throw new MessageOnlyException(s"Akka HTTP requires Akka 2.5 or later but was $akkaVersion")
    } else {
      // everything is OK as far as we know
    }
  }

}
