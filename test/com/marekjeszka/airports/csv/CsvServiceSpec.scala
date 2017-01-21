package com.marekjeszka.airports.csv

import com.marekjeszka.airports.DataImporter
import com.typesafe.config.ConfigFactory
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

  it should "query top countries grouped by number of airports" in {
    val csvService = new CsvService(TopCountriesMockImporter)
    val topCountries = csvService.queryTopCountries(2)
    topCountries.size should be (2)
    topCountries(0) should be ((Map("name" -> "Poland", "code" -> "PL"), 2))
    topCountries(1) should be ((Map("name" -> "Canada", "code" -> "CA"), 2))

    val lowCountries = csvService.queryTopCountries(2, false)
    lowCountries.size should be (2)
    lowCountries(0) should be ((Map("name" -> "Liberia", "code" -> "LR"), 1))
    lowCountries(1) should be ((Map("name" -> "Lithuania", "code" -> "LT"), 1))
  }

  it should "query surfaces per country" in {
    val csvService = new CsvService(RunwaysPerCountryMockImporter)
    val runwaysPerCountry = csvService.queryRunwaysPerCountry()
    runwaysPerCountry.size should be (2)
    runwaysPerCountry(0) should be (("Poland", List("TURF-F", "TURF", "CONC")))
    runwaysPerCountry(1) should be (("Canada", List("ASPH")))
  }

  it should "query top runway identification" in {
    val csvService = new CsvService(RunwaysIdentMockImporter)
    val topIdent = csvService.queryTopRunwayIdentifications(limit = 2)
    topIdent.size should be (2)
    topIdent(0) should be ("H1")
    topIdent(1) should be ("12")
  }

  private class MockImporter(data: List[Map[String, String]]) extends DataImporter {
    override def loadData(path: String): (List[String], List[Map[String, String]]) =
      (Nil, data)
  }

  private object TopCountriesMockImporter extends DataImporter {
    private val conf = ConfigFactory.load()

    override def loadData(path: String): (List[String], List[Map[String, String]]) = {
      if (path == conf.getString("paths.countries")) {
        (Nil, List(
          Map("name" -> "Liberia", "code" -> "LR"),
          Map("name" -> "Lithuania", "code" -> "LT"),
          Map("name" -> "Poland", "code" -> "PL"),
          Map("name" -> "Canada", "code" -> "CA")))
      } else { // airports
        (Nil, List(
          Map("id" -> "11002", "iso_country" -> "PL"),
          Map("id" -> "11003", "iso_country" -> "PL"),
          Map("id" -> "11004", "iso_country" -> "CA"),
          Map("id" -> "11005", "iso_country" -> "CA"),
          Map("id" -> "11006", "iso_country" -> "LT"),
          Map("id" -> "11007", "iso_country" -> "LR")))
      }
    }
  }

  private object RunwaysPerCountryMockImporter extends DataImporter {
    private val conf = ConfigFactory.load()

    override def loadData(path: String): (List[String], List[Map[String, String]]) = {
      if (path == conf.getString("paths.countries")) {
        (Nil, List(
          Map("name" -> "Poland", "code" -> "PL"),
          Map("name" -> "Canada", "code" -> "CA")))
      } else if (path == conf.getString("paths.airports")) {
        (Nil, List(
          Map("id" -> "11002", "iso_country" -> "PL"),
          Map("id" -> "11003", "iso_country" -> "PL"),
          Map("id" -> "11004", "iso_country" -> "CA"),
          Map("id" -> "11005", "iso_country" -> "CA")))
      } else { // runways
        (Nil, List(
          Map("airport_ref" -> "11002", "surface" -> "TURF-F"),
          Map("airport_ref" -> "11002", "surface" -> "TURF"),
          Map("airport_ref" -> "11003", "surface" -> "CONC"),
          Map("airport_ref" -> "11005", "surface" -> "ASPH")))
      }
    }
  }

  private object RunwaysIdentMockImporter extends DataImporter {
    private val conf = ConfigFactory.load()

    override def loadData(path: String): (List[String], List[Map[String, String]]) = {
      (Nil, List(
        Map("le_ident" -> "H1"),
        Map("le_ident" -> "H1"),
        Map("le_ident" -> "H1"),
        Map("le_ident" -> "12"),
        Map("le_ident" -> "12"),
        Map("le_ident" -> "08")))
    }
  }
}