name := "sample"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  "com.feth" %% "play-temporary-storage" % "0.0.1-SNAPSHOT"
)     

play.Project.playJavaSettings