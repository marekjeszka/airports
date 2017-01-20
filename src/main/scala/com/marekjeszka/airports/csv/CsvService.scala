package com.marekjeszka.airports.csv

import com.marekjeszka.airports.{DataImporter, DataService}
import com.typesafe.config.ConfigFactory

class CsvService(importer: DataImporter = CsvImporter) extends DataService {
  private val conf = ConfigFactory.load()
  private val countries = importer.loadData(conf.getString("paths.countries"))

  override def queryCountry(name: String): List[(String,String)] = {
    countries._2.filter(m => m("name").startsWith(name)).map(c => (c("code"),c("name")))
  }
}
