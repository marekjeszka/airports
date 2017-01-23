package com.marekjeszka.airports

import com.google.inject.ImplementedBy
import com.marekjeszka.airports.csv.CsvImporter

@ImplementedBy(classOf[CsvImporter])
trait DataImporter {
  def loadData(path: String): List[Map[String, String]]
}
