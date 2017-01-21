package com.marekjeszka.airports

trait DataImporter {
  def loadData(path: String): List[Map[String, String]]
}
