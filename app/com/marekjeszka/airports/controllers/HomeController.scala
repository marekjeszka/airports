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
}
