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

class VrnSpec extends AnyWordSpec with Matchers with LoneElement {

  "Vrn constructor" should {
    "validates the valid VRN" in {
      Vrn.isValid(Vrn("123456789").vrn) shouldBe true
    }

    "validates the obsolete Mongo VRN" in {
      Vrn.isValid(Vrn("12345").vrn) shouldBe true
    }

    "fails an invalid VRN" in {
      val ex = the[IllegalArgumentException] thrownBy Vrn("1234")
      ex should have(Symbol("message")("requirement failed: 1234 is not a valid vrn."))
    }
  }

  "deserialising json to Vrn" should {

    "succeed to build a Vrn" in {
      val input =
        """{
          |"vrn": "123456789"
        }""".stripMargin
      Json.parse(input).as[Vrn] shouldBe Vrn("123456789")
    }

    "return JsError is there are no tax identifiers in the json" in {
      val input = "{}"
      Json.parse(input).validate[Vrn] shouldBe a[JsError]
    }
  }

  "serialising Vrn to json" should {
    "generate valid json for a Vrn" in {
      implicit val format: Format[TaxIds] = TaxIds.format(TaxIds.defaultSerialisableIds*)
      val input = TaxIds(Vrn("123456789"))
      Json.toJson(input).toString shouldBe """{"vrn":"123456789"}"""
    }
  }
}
