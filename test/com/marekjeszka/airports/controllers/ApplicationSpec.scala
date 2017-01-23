package com.marekjeszka.airports.controllers

import com.marekjeszka.airports.{DataImporter, DataService}
import com.marekjeszka.airports.csv.{CsvImporter, CsvService}
import com.marekjeszka.airports.model.CountryAirportRunway
import org.scalatestplus.play._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test._

class ApplicationSpec extends PlaySpec with OneAppPerSuite {

  implicit override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[DataService].to[MockCsvService])
    .build()

  "Routes" should {
    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status) mustBe Some(NOT_FOUND)
    }
  }

  "HomeController" should {
    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play!")
    }
    "render JSON on /countries/:name" in {
      val response = route(app, FakeRequest(GET, "/countries/Poland")).get
      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) mustBe """[{"Poland":{"Lawica":[{"surface":"ASPH"}]}}]"""
    }
    "render JSON on /reports/topCountries" in {
      val response = route(app, FakeRequest(GET, "/reports/topCountries")).get
      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) mustBe """[[{"Poland":3},{"Germany":5}],[{"Poland":3},{"Germany":5}]]"""
    }
    "render JSON on /reports/runwaysPerCountry" in {
      val response = route(app, FakeRequest(GET, "/reports/runwaysPerCountry")).get
      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) mustBe """[{"Poland":["GRAVEL","GRASS"]},{"Germany":["TURF"]}]"""
    }
    "render JSON on /reports/topIdentifications" in {
      val response = route(app, FakeRequest(GET, "/reports/topIdentifications")).get
      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) mustBe """["H1","H2"]"""
    }
  }

}

class MockCsvService extends DataService {
  override def queryAirportsWithRunways(countryName: String): List[CountryAirportRunway] =
    List(CountryAirportRunway("Poland", List(("Lawica", List(Map("surface" -> "ASPH"))))))

  override def queryTopCountries(limit: Int, descending: Boolean): List[(Map[String, String],Int)] =
    List((Map("name" -> "Poland"),3),(Map("name" -> "Germany"),5))

  override def queryRunwaysPerCountry(): List[(String, List[String])] =
    List(("Poland",List("GRAVEL", "GRASS")),("Germany",List("TURF")))

  override def queryTopRunwayIdentifications(limit: Int): List[String] = List("H1", "H2")
}
