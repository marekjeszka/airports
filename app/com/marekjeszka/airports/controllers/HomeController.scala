package com.marekjeszka.airports.controllers

import javax.inject._

import com.marekjeszka.airports.DataService
import com.marekjeszka.airports.csv.CsvService
import com.marekjeszka.airports.model.CountryAirportRunway
import play.api.libs.json._
import play.api.mvc._

@Singleton
class HomeController @Inject() extends Controller {

  private val dataService: DataService = new CsvService()

  def index = Action {
    Ok(com.marekjeszka.airports.views.html.index())
  }

  def countries(name: String) = Action {
    import CountryAirportRunway._
    val json: JsValue = Json.toJson(dataService.queryAirportsWithRunways(name))
    Ok(json)
  }

  private def countryWithIntToJsonObject = (c: (Map[String, String],Int)) => JsObject(Seq(c._1("name") -> JsNumber(c._2)))

  def topCountries = Action {
    val top = JsArray(dataService.queryTopCountries().map(countryWithIntToJsonObject))
    val low = JsArray(dataService.queryTopCountries(descending = false).map(countryWithIntToJsonObject))
    Ok(JsArray(Seq(top,low)))
  }

  private def runwaysPerCountryToJsonObject = (c: (String, List[String])) => JsObject(Seq(c._1 -> JsArray(c._2.map(JsString))))

  def runwaysPerCountry = Action {
    Ok(JsArray(dataService.queryRunwaysPerCountry().map(runwaysPerCountryToJsonObject)))
  }

  def topIdentifications = Action {
    Ok(JsArray(dataService.queryTopRunwayIdentifications().map(JsString)))
  }
}
