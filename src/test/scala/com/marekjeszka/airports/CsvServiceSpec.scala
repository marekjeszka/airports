package com.marekjeszka.airports

import com.marekjeszka.airports.csv.CsvService
import org.scalatest.{FlatSpec, Matchers}

class CsvServiceSpec extends FlatSpec with Matchers {
  "CsvService" should "query countries partially/fuzzy" in {
    val csvService = new CsvService(MockImporter)
    val countries = csvService.queryCountry("Li")
    countries(0) should be(("LR", "Liberia"))
    countries(1) should be(("LT", "Lithuania"))
  }

  private object MockImporter extends DataImporter {
    override def loadData(path: String): (List[String], List[Map[String, String]]) = {
      (Nil,
        List(
          Map("name" -> "Liberia", "code" -> "LR"),
          Map("name" -> "Lithuania", "code" -> "LT")))
    }
  }

}
