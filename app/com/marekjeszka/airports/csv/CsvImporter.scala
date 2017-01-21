package com.marekjeszka.airports.csv

import java.io.File

import com.github.tototoshi.csv.CSVReader
import com.marekjeszka.airports.DataImporter

object CsvImporter extends DataImporter {
  override def loadData(path: String): List[Map[String, String]] = {
    val reader = CSVReader.open(new File(path))
    val (fields, data) = reader.allWithOrderedHeaders()
    data.filter(p => fields.forall(p.contains))
  }
}
