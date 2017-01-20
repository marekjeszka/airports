package com.marekjeszka.airports.csv

import org.scalatest.{FlatSpec, Matchers}

class CsvImporterSpec extends FlatSpec with Matchers {
  "CsvImporter" should "import airports" in {
    val airports = CsvImporter.loadData("src/test/resources/airports.csv")

    airports._2.length should be (3)
    Seq("ident" -> "42MD", "type" -> "small_airport").foreach {
      airports._2.head should contain(_)
    }
    Seq("name" -> "Bulgan Sum Airport", "elevation_ft" -> "3921").foreach {
      airports._2.last should contain(_)
    }

    airports._1 should be (List("id","ident","type","name","latitude_deg","longitude_deg","elevation_ft","continent","iso_country","iso_region","municipality","scheduled_service","gps_code","iata_code","local_code","home_link","wikipedia_link","keywords"))
  }
}
