package com.marekjeszka.airports

trait DataImporter {
  def loadData(path: String): (List[String], List[Map[String, String]])
}
