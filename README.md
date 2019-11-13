# sbt-akka-version-check

A plugin that verifies Akka module versions of a project

## Usage

This plugin requires sbt 1.0.0+

Add the plugin to your project, for example in `project/plugins.sbt`:

*Note WIP*: not actually published yet

```
addSbtPlugin("com.lightbend.akka" % "sbt-akka-version-check" % "0.1")
```

Trigger the version check through the `checkAkkaModuleVersions` task. The check will fail the build if there
are problems with version conflicts in the Akka modules used in the project. 

The task can be useful to put in your PR validation on a CI-server to detect if changes accidentally
introduce transitive Akka module dependencies of mismatched versions.

### Testing

Run `test` for regular unit tests.

Run `scripted` for [sbt script tests](http://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html).

### Publishing

1. publish your source to GitHub
2. [create a bintray account](https://bintray.com/signup/index) and [set up bintray credentials](https://github.com/sbt/sbt-bintray#publishing)
3. create a bintray repository `sbt-plugins` 
4. update your bintray publishing settings in `build.sbt`
5. `sbt publish`
6. [request inclusion in sbt-plugin-releases](https://bintray.com/sbt/sbt-plugin-releases)
7. [Add your plugin to the community plugins list](https://github.com/sbt/website#attention-plugin-authors)
8. [Claim your project an Scaladex](https://github.com/scalacenter/scaladex-contrib#claim-your-project)
