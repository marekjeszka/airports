package com.marekjeszka.airports

import com.google.inject.ImplementedBy
import com.marekjeszka.airports.csv.CsvService
import com.marekjeszka.airports.model.CountryAirportRunway

@ImplementedBy(classOf[CsvService])
trait DataService {
  def queryAirportsWithRunways(countryName: String): List[CountryAirportRunway]

  def queryTopCountries(limit: Int = 10, descending: Boolean = true): List[(Map[String, String],Int)]

  def queryRunwaysPerCountry(): List[(String,List[String])]

  def queryTopRunwayIdentifications(limit: Int = 10): List[String]
}
