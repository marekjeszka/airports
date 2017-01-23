import scoverage.ScoverageKeys.coverageExcludedPackages

name := "airports"

version := "1.0"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

coverageExcludedFiles := ".*Routes.*;.*template.*"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  Seq(
	filters,
    "com.typesafe" % "config" % "1.3.1",
    "com.github.tototoshi" %% "scala-csv" % "1.3.4",

    "org.scalatest" %% "scalatest" % "3.0.1" % Test,
	  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
  )
}
