name := "airports"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  Seq(
    "com.typesafe" % "config" % "1.3.1",
    "com.github.tototoshi" %% "scala-csv" % "1.3.4",

    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
}