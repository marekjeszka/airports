package com.marekjeszka.airports.csv

import com.marekjeszka.airports.{DataImporter, DataService}
import com.typesafe.config.ConfigFactory

class CsvService(importer: DataImporter = CsvImporter) extends DataService {
  private val conf = ConfigFactory.load()
  private val countries = importer.loadData(conf.getString("paths.countries"))
  private val airports = importer.loadData(conf.getString("paths.airports"))
  private val runways = importer.loadData(conf.getString("paths.runways"))

  override def queryCountries(name: String): List[(String,String)] = {
    countries._2.filter(m => m("name").startsWith(name)).map(c => (c("code"),c("name")))
  }

  override def queryAirports(iso_country: String): List[Map[String, String]] = {
    isoCountryQuery(iso_country)(airports._2)
  }

  override def queryRunways(iso_country: String): List[Map[String, String]] = {
    isoCountryQuery(iso_country)(runways._2)
  }

  private def isoCountryQuery(iso_country: String)(from: List[Map[String,String]]) = {
    from.filter(m => m("iso_country") == iso_country)
  }
}
