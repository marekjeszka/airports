package com.marekjeszka.airports

import com.marekjeszka.airports.csv.CsvService
import org.scalatest.{FlatSpec, Matchers}

class CsvServiceSpec extends FlatSpec with Matchers {
  "CsvService" should "query countries partially/fuzzy" in {
    val csvService = new CsvService(new MockImporter(
      List(
        Map("name" -> "Liberia", "code" -> "LR"),
        Map("name" -> "Lithuania", "code" -> "LT"),
        Map("name" -> "Poland", "code" -> "PL"))))
    val countries = csvService.queryCountries("Li")
    countries.size should be (2)
    countries(0) should be(("LR", "Liberia"))
    countries(1) should be(("LT", "Lithuania"))
  }

  it should "query airports" in {
    val pl1 = Map("id" -> "11002", "iso_country" -> "PL")
    val pl2 = Map("id" -> "11003", "iso_country" -> "PL")
    val csvService = new CsvService(new MockImporter(
      List(
        Map("id" -> "11001", "iso_country" -> "GB"),
        pl1, pl2)))
    val airports = csvService.queryAirports("PL")
    airports.size should be (2)
    airports(0) should be(pl1)
    airports(1) should be(pl2)
  }

  private class MockImporter(data: List[Map[String, String]]) extends DataImporter {
    override def loadData(path: String): (List[String], List[Map[String, String]]) =
      (Nil, data)
  }

}
