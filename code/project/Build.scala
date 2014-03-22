import sbt._
import Keys._
import play.Project._

object Build extends sbt.Build {

    val appName         = "play-temporary-storage"
    val appVersion      = "0.0.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
        cache
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      organization := "com.feth",
      resolvers += "Apache" at "http://repo1.maven.org/maven2/",
      libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"
    )
}