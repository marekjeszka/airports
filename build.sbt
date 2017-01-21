name := "airports"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= {
  Seq(
	filters,
    "com.typesafe" % "config" % "1.3.1",
    "com.github.tototoshi" %% "scala-csv" % "1.3.4",

    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
	"org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
  )
}
