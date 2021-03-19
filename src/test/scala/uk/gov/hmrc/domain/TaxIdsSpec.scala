/*
 * Copyright 2021 HM Revenue & Customs
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

import org.scalatest.{LoneElement, Matchers, WordSpec}
import play.api.libs.json.{JsError, Json}

class TaxIdsSpec extends WordSpec with Matchers with LoneElement {

  val nino = Nino("NM439088A")
  val saUtr = SaUtr("some-sa-utr")
  implicit val format = TaxIds.format(TaxIds.defaultSerialisableIds: _*)

  "TaxIds constructor" should {
    "fail to create a TaxIds object with duplicate tax identifiers" in {
      val ex = the[IllegalArgumentException] thrownBy TaxIds(nino, nino)
      ex should have('message("requirement failed: TaxIds cannot have TaxIdentifers with the same name"))
    }

    "fail to create a TaxIds object with no tax identifiers" in {
      val ex = the[IllegalArgumentException] thrownBy TaxIds()
      ex should have('message("requirement failed: TaxIds must have at least one TaxIdentifier"))
    }
  }

  "deserialising json to TaxIds" should {

    "succeed to build a TaxIds object with a TaxIdentifier of type Nino" in {
      val input =
        """{
          |"nino": "NM439088A"
        }""".stripMargin
      Json.parse(input).as[TaxIds] should have('nino(Some(nino)))
    }

    "succeed to build a TaxIds object with two TaxIdentifiers of type Nino and SaUtr" in {
      val input =
        """{
          |"nino": "NM439088A",
          |"sautr": "some-sa-utr"
        }""".stripMargin
      Json.parse(input).as[TaxIds] should (have('nino(Some(nino))) and have('saUtr(Some(saUtr))))
    }

    "succeed to build a TaxIds object with a TaxIdentifier of type SaUtr" in {
      val input =
        """{
          |"sautr": "some-sa-utr"
        }""".stripMargin
      Json.parse(input).as[TaxIds] should have('saUtr(Some(saUtr)))
    }

    "fail to build a TaxIds object with more than one TaxIdentifier of the same type" ignore {
      val input =
        """{
          |"sautr": "some-sa-utr",
          |"sautr": "some-other-sa-utr"
        }""".stripMargin
      Json.parse(input).validate shouldBe a[JsError]
    }

    "fail to build a TaxIds object with no TaxIdentifier" in {
      val input =
        """{}""".stripMargin
      Json.parse(input).validate shouldBe a[JsError]
    }

    "ignore unkown TaxIdentifiers" in {
      val input =
        """{
          |"sautr": "some-sa-utr",
          |"unkownTaxId": "654654"
        }""".stripMargin
      Json.parse(input).as[TaxIds].values.loneElement shouldBe (saUtr)
    }

    "succeed to build a TaxIds object with a new custom TaxIdentifier" in {
      case class CustomTaxId(customId: String) extends TaxIdentifier with SimpleName {
        override def value: String = customId
        override val name: String = "customTaxId"
      }
      implicit val format = TaxIds.format(SerialisableTaxId("customTaxId", CustomTaxId.apply))
      val expected = CustomTaxId("some-custom-value")
      val input =
        """{
          |"customTaxId": "some-custom-value"
        }""".stripMargin
      Json.parse(input).as[TaxIds].values should contain(expected)
    }

    "return JsError is there are no tax identifiers in the json" in {
      val input = "{}"
      Json.parse(input).validate[TaxIds] shouldBe a[JsError]
    }
  }

  "serialising TaxIds to json" should {
    "generate valid json for a TaxIds object with a TaxIdentifier of type Nino" in {
      val input = TaxIds(nino)
      (Json.toJson(input) \ "nino").as[String] shouldBe nino.value
    }

    "generate valid json for a TaxIds object with two TaxIdentifiers of type Nino and SaUtr" in {
      val input = TaxIds(nino, saUtr)
      (Json.toJson(input) \ "nino").as[String] shouldBe nino.value
      (Json.toJson(input) \ "sautr").as[String] shouldBe saUtr.value
    }

    "generate valida json ignoring non-serialisable TaxIdentifiers" in {
      val unknownId = new TaxIdentifier with SimpleName {
        override val name: String = "unknownId"
        override def value: String = "654654"
      }
      val input = TaxIds(nino, saUtr, unknownId)
      val json = Json.toJson(input)
      (json \ "nino").as[String] shouldBe nino.value
      (json \ "sautr").as[String] shouldBe saUtr.value
      (json \ "unknownId").asOpt[String] shouldNot be(defined)
    }

    "generate valid json for a TaxIds object with a TaxIdentifier of type SaUtr" in {
      val input = TaxIds(saUtr)
      (Json.toJson(input) \ "sautr").as[String] shouldBe saUtr.value
    }

  }

  "extracting specific tax identifiers from TaxIds" should {

    "return a nino is TaxIds contains one" in {
      val taxIdssWithNino = TaxIds(nino)
      taxIdssWithNino.nino shouldBe Some(nino)
      taxIdssWithNino.saUtr shouldBe None
    }

    "return a saUtr is TaxIds contains one" in {
      val taxIdssWithNino = TaxIds(saUtr)
      taxIdssWithNino.saUtr shouldBe Some(saUtr)
      taxIdssWithNino.nino shouldBe None
    }

    "return a saUtr and a nino is TaxIdss contains both" in {
      val taxIdssWithNino = TaxIds(saUtr, nino)
      taxIdssWithNino.nino shouldBe Some(nino)
      taxIdssWithNino.saUtr shouldBe Some(saUtr)
    }
  }
}
