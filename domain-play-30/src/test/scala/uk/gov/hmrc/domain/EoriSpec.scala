/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.domain

import org.scalatest.LoneElement
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{Format, JsError, Json}

class EoriSpec extends AnyWordSpec with Matchers with LoneElement {

  "Eori constructor" should {
    "validates the valid Eori" in {
      Eori.isValid(Eori("GB123456789001").eori) shouldBe true
    }

    "fails an invalid Eori" in {
      val ex = the[IllegalArgumentException] thrownBy Eori("1234")
      ex should have(Symbol("message")("requirement failed: 1234 is not a valid eori."))
    }
  }

  "deserialising json to Eori" should {

    "succeed to build a Eori" in {
      val input =
        """{
          |"eori": "GB123456789001"
        }""".stripMargin
      Json.parse(input).as[Eori] shouldBe Eori("GB123456789001")
    }

    "return JsError is there are no tax identifiers in the json" in {
      val input = "{}"
      Json.parse(input).validate[Eori] shouldBe a[JsError]
    }
  }

  "serialising Eori to json" should {
    "generate valid json for an Eori" in {
      implicit val format: Format[TaxIds] = TaxIds.format(TaxIds.defaultSerialisableIds*)
      val input = TaxIds(Eori("GB123456789001"))
      Json.toJson(input).toString shouldBe """{"eori":"GB123456789001"}"""
    }
  }
}
