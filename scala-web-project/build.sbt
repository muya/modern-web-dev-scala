name := """scala-web-project"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
pipelineStages := Seq(digest)

PlayKeys.fileWatchService := play.runsupport.FileWatchService.sbt(2000)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
updateOptions := updateOptions.value.withCachedResolution(true)

val buildSettings = Defaults.defaultSettings ++ Seq(
  javaOptions += "-Xmx1G",
  javaOptions += "-Xdebug",
  javaOptions += "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)
