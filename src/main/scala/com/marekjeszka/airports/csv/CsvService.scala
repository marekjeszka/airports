package com.marekjeszka.airports.csv

import com.marekjeszka.airports.{DataImporter, DataService}
import com.typesafe.config.ConfigFactory

import scala.collection.immutable.ListMap

class CsvService(importer: DataImporter = CsvImporter) extends DataService {
  import CsvService._

  private val conf = ConfigFactory.load()
  private val countries = importer.loadData(conf.getString("paths.countries"))
  private val airports = importer.loadData(conf.getString("paths.airports"))
  private val runways = importer.loadData(conf.getString("paths.runways"))

  override def queryCountries(countryName: String): List[(String,String)] = {
    countries._2.filter(m => m(Name).startsWith(countryName)).map(c => (c(Code),c(Name)))
  }

  override def queryAirports(iso_country: String): List[Map[String, String]] = {
    isoCountryQuery(iso_country)(airports._2)
  }

  override def queryRunways(iso_country: String): List[Map[String, String]] = {
    isoCountryQuery(iso_country)(runways._2)
  }

  private def isoCountryQuery(iso_country: String)(from: List[Map[String,String]]) = {
    from.filter(m => m(Iso_country) == iso_country)
  }

  override def queryTopCountries(limit: Int, descending: Boolean): List[(Map[String, String],Int)] = {
    val airportsCount = sizeOfGroup(airports._2, Iso_country)
    val ordered = ListMap(airportsCount.toSeq.sortWith(ordering(descending)):_*).take(limit)
    countries._2
      .filter(c => ordered.contains(c(Code)))
      .map(c => (c, ordered(c(Code))))
  }

  override def queryRunwaysPerCountry(): List[(String,List[String])] = {
    countries._2.map(c => (c(Name), c(Code)))
      .map(c => (c._1,
        airports._2.filter(a => a(Iso_country) == c._2).map(a => a(Id))))
      .map(c => (c._1,
        c._2
          .flatMap(
            airportId => runways._2.filter(r => r(Airport_ref) == airportId)
          )
          .map(r => r(Surface))))
  }

  override def queryTopRunwayIdentifications(limit: Int): List[String] = {
    val identsCount = sizeOfGroup(runways._2, Le_ident)
    val ordered = ListMap(identsCount.toSeq.sortWith(ordering(true)):_*).take(limit)
    ordered.keys.toList
  }

  private def sizeOfGroup(col: List[Map[String,String]], field: String): Map[String, Int] = {
    col
      .groupBy(r => r(field))
      .map(g => (g._1, g._2.size))
  }

  private def ordering(descending: Boolean): ((String,Int), (String,Int)) => Boolean = {
    if (descending)
      (a, b) => a._2 > b._2
    else
      (a, b) => a._2 < b._2
  }
}

object CsvService {
  // used CSV fields
  private val Name = "name"
  private val Iso_country = "iso_country"
  private val Code = "code"
  private val Id = "id"
  private val Airport_ref = "airport_ref"
  private val Surface = "surface"
  private val Le_ident = "le_ident"
}
