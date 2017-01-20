package com.marekjeszka.airports

trait DataService {
  def queryCountry(name: String): List[(String,String)]
}
