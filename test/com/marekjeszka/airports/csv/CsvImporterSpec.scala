package com.marekjeszka.airports.csv

import org.scalatest.{FlatSpec, Matchers}

class CsvImporterSpec extends FlatSpec with Matchers {
  "CsvImporter" should "import airports" in {
    val airports = CsvImporter.loadData("test/resources/airports.csv")

    airports.length should be (3)
    Seq("ident" -> "42MD", "type" -> "small_airport").foreach {
      airports.head should contain(_)
    }
    Seq("name" -> "Bulgan Sum Airport", "elevation_ft" -> "3921").foreach {
      airports.last should contain(_)
    }
  }
}
