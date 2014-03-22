name := "sample"

version := "1.0-SNAPSHOT"

resolvers += Resolver.url("play-temporary-storage (release)", url("http://joscha.github.com/play-temporary-storage/repo/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("play-temporary-storage (snapshot)", url("http://joscha.github.com/play-temporary-storage/repo/snapshots/"))(Resolver.ivyStylePatterns)


libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  "com.feth" %% "play-temporary-storage" % "0.0.1-SNAPSHOT"
)

play.Project.playJavaSettings
