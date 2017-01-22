package com.marekjeszka.airports.model

import play.api.libs.json.{JsArray, JsObject, JsString, Writes}

import scala.collection.immutable.List

case class CountryAirportRunway(countryName: String, airportsRunways: List[(String, List[Map[String,String]])])

object CountryAirportRunway {

  implicit object CountryAirportRunwayFormat extends Writes[CountryAirportRunway] {
    def writes(country: CountryAirportRunway) = JsObject(Seq(
      country.countryName ->
        JsObject(country.airportsRunways.map(a => a._1 ->
          JsArray(a._2.map(r => JsObject(r.toSeq.map(f => f._1 -> JsString(f._2)))))
        ))))
  }
}
