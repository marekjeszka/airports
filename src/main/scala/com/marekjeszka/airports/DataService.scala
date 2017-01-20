package com.marekjeszka.airports

trait DataService {
  /**
    * Returns list of countries for given name.
    * @param name name of te country (works partially - starts with)
    * @return (code,name)
    */
  def queryCountries(name: String): List[(String,String)]

  /**
    * Returns list of airports for given country code.
    * @param iso_country country code
    * @return airports as map: field -> value
    */
  def queryAirports(iso_country: String): List[Map[String, String]]

  /**
    * Returns list of runways for given country code.
    * @param iso_country country code
    * @return runways as map: field -> value
    */
  def queryRunways(iso_country: String): List[Map[String, String]]
}
