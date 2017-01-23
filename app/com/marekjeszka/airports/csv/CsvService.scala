package com.marekjeszka.airports.csv

import com.google.inject.Inject
import com.marekjeszka.airports.model.CountryAirportRunway
import com.marekjeszka.airports.{DataImporter, DataService}
import com.typesafe.config.ConfigFactory

import scala.collection.immutable.ListMap

class CsvService @Inject() (implicit importer: DataImporter) extends DataService {
  import CsvService._

  private val conf = ConfigFactory.load()
  private val allCountries = importer.loadData(conf.getString("paths.countries"))
  private val allAirports = importer.loadData(conf.getString("paths.airports"))
  private val allRunways = importer.loadData(conf.getString("paths.runways"))

  /**
    * Returns list of countries for given code or name (works partially - starts with).
    * @param countryName name or code of the country
    * @return (code,name)
    */
  def queryCountries(countryName: String): List[(String,String)] = {
    allCountries
      .filter(m => m(Name).startsWith(countryName) || m(Code) == countryName)
      .map(c => (c(Code),c(Name)))
  }

  /**
    * Returns list of airports for given country code.
    * @param iso_country country code
    * @return airports as map: field -> value
    */
  def queryAirports(iso_country: String): List[Map[String, String]] = {
    allAirports.filter(m => m(Iso_country) == iso_country)
  }

  /**
    * Returns list of runways for given airport ref.
    * @param airport_ref airport ref
    * @return runways as map: field -> value
    */
  def queryRunways(airport_ref: String): List[Map[String, String]] = {
    allRunways.filter(m => m(Airport_ref) == airport_ref)
  }

  override def queryAirportsWithRunways(countryName: String): List[CountryAirportRunway] = {
    queryCountries(countryName).map(c =>
      CountryAirportRunway(
        c._2,
        queryAirports(c._1).map(a => (a(Name), queryRunways(a(Id))))))
  }

  override def queryTopCountries(limit: Int, descending: Boolean): List[(Map[String, String],Int)] = {
    val airportsCount = sizeOfGroup(allAirports, Iso_country)
    val ordered = ListMap(airportsCount.toSeq.sortWith(ordering(descending)):_*).take(limit)
    allCountries
      .filter(c => ordered.contains(c(Code)))
      .map(c => (c, ordered(c(Code))))
  }

  override def queryRunwaysPerCountry(): List[(String,List[String])] = {
    allCountries.map(c => (c(Name), c(Code)))
      .map(c => (c._1,
        allAirports.filter(a => a(Iso_country) == c._2).map(a => a(Id))))
      .map(c => (c._1,
        c._2
          .flatMap(
            airportId => allRunways.filter(r => r(Airport_ref) == airportId)
          )
          .map(r => r(Surface)).distinct))
  }

  override def queryTopRunwayIdentifications(limit: Int): List[String] = {
    val identsCount = sizeOfGroup(allRunways, Le_ident)
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
