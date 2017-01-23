package com.marekjeszka.airports.controllers

import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._

class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {
    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status) mustBe Some(NOT_FOUND)
    }
  }
}
