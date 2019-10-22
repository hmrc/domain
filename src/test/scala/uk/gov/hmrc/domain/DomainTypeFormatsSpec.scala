/*
 * Copyright 2019 HM Revenue & Customs
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

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._

class DomainTypeFormatsSpec extends WordSpec with Matchers {

  "Vrn reader" should {

    "be able to read obsolete Vrn structure from Mongo" in {
      val dbStructure = JsObject(Seq("vrn" -> JsString("12345")))
      val result = Vrn.vrnRead.reads(dbStructure)
      result.get shouldBe Vrn("12345")
    }

    "be able to read string representation of Vrn" in {
      val restStructure = JsString("12345")
      val result = Vrn.vrnRead.reads(restStructure)
      result.get shouldBe Vrn("12345")
    }
  }

  "SaUtr reader" should {

    "be able to read obsolete SaUtr structure from Mongo" in {
      val dbStructure = JsObject(Seq("utr" -> JsString("12345")))
      val result = SaUtr.saUtrRead.reads(dbStructure)
      result.get shouldBe SaUtr("12345")
    }

    "be able to read string representation of SaUtr" in {
      val restStructure = JsString("12345")
      val result = SaUtr.saUtrRead.reads(restStructure)
      result.get shouldBe SaUtr("12345")
    }
  }

  "CtUtr reader" should {

    "be able to read obsolete CtUtr structure from Mongo" in {
      val dbStructure = JsObject(Seq("utr" -> JsString("12345")))
      val result = CtUtr.ctUtrRead.reads(dbStructure)
      result.get shouldBe CtUtr("12345")
    }

    "be able to read string representation of CtUtr" in {
      val restStructure = JsString("12345")
      val result = CtUtr.ctUtrRead.reads(restStructure)
      result.get shouldBe CtUtr("12345")
    }
  }

  "AtedUtr reader" should {

    "be able to read obsolete AtedUtr structure from Mongo" in {
      val dbStructure = JsObject(Seq("utr" -> JsString("XN1200000100001")))
      val result = AtedUtr.atedUtrRead.reads(dbStructure)
      result.get shouldBe AtedUtr("XN1200000100001")
    }

    "be able to read obsolete new AtedUtr structure from Mongo" in {
      val dbStructure = JsObject(Seq("utr" -> JsString("XTAT00000100001")))
      val result = AtedUtr.atedUtrRead.reads(dbStructure)
      result.get shouldBe AtedUtr("XTAT00000100001")
    }

    "be able to read string representation of AtedUtr" in {
      val restStructure = JsString("XN1200000100001")
      val result = AtedUtr.atedUtrRead.reads(restStructure)
      result.get shouldBe AtedUtr("XN1200000100001")
    }

    "be able to read string representation of new AtedUtr" in {
      val restStructure = JsString("XTAT00000100001")
      val result = AtedUtr.atedUtrRead.reads(restStructure)
      result.get shouldBe AtedUtr("XTAT00000100001")
    }

  }

  "Uar reader" should {

    "be able to read obsolete Uar structure from Mongo" in {
      val dbStructure = JsObject(Seq("uar" -> JsString("12345")))
      val result = Uar.uarRead.reads(dbStructure)
      result.get shouldBe Uar("12345")
    }

    "be able to read string representation of Uar" in {
      val restStructure = JsString("12345")
      val result = Uar.uarRead.reads(restStructure)
      result.get shouldBe Uar("12345")
    }
  }

  "AgentCode reader" should {

    "be able to read obsolete Agent Code structure from Mongo" in {
      val dbStructure = JsObject(Seq("agentCode" -> JsString("12345")))
      val result = AgentCode.agentCodeRead.reads(dbStructure)
      result.get shouldBe AgentCode("12345")
    }

    "be able to read string representation of Agent Code" in {
      val restStructure = JsString("12345")
      val result = AgentCode.agentCodeRead.reads(restStructure)
      result.get shouldBe AgentCode("12345")
    }
  }

  "AgentUserId reader" should {

    "be able to read obsolete Agent User ID structure from Mongo" in {
      val dbStructure = JsObject(Seq("agentUserId" -> JsString("12345")))
      val result = AgentUserId.agentUserIdRead.reads(dbStructure)
      result.get shouldBe AgentUserId("12345")
    }

    "be able to read string representation of Agent User Id" in {
      val restStructure = JsString("12345")
      val result = AgentUserId.agentUserIdRead.reads(restStructure)
      result.get shouldBe AgentUserId("12345")
    }
  }

 "Nino reader" should {

    "be able to read obsolete Nino structure from Mongo" in {
      val dbStructure = JsObject(Seq("nino" -> JsString("AB123456D")))
      val result = Nino.ninoRead.reads(dbStructure)
      result.get shouldBe Nino("AB123456D")
    }

   "return JsError if the nino is invalid" in {
     val dbStructure = JsObject(Seq("nino" -> JsString("AB123456")))
     val result = Nino.ninoRead.reads(dbStructure)
     result shouldBe a[JsError]
   }

    "be able to read string representation of Nino" in {
      val restStructure = JsString("ZY987654C")
      val result = Nino.ninoRead.reads(restStructure)
      result.get shouldBe Nino("ZY987654C")
    }
  }


  "EmpRef reader" should {

    "be able to read obsolete EmpRef structure from Mongo" in {
      val dbStructure = JsObject(Seq("taxOfficeNumber" -> JsString("12345"), "taxOfficeReference" -> JsString("ref")))
      val result = EmpRef.empRefRead.reads(dbStructure)
      result.get shouldBe EmpRef("12345", "ref")
    }

    "be able to read string representation of EmpRef" in {
      val restStructure = JsString("12345/ref")
      val result = EmpRef.empRefRead.reads(restStructure)
      result.get shouldBe EmpRef("12345", "ref")
    }

    s"be able to read an employer reference from a play-authorised-frontend EpayeAccount case class"  in {
      val epayeAccount = Json.obj("links" -> "/epaye/12345%2Fref", "empRef" -> "12345/ref")
      val result = EmpRef.empRefRead.reads(epayeAccount)
      result.get shouldBe EmpRef("12345", "ref")
    }

  }

  "AgentBusinessUtr reader" should {

    "be able to read obsolete AgentBusinessUtr from Mongo" in {
      val dbStructure = JsObject(Seq("utr" -> JsString("JARN1234567")))
      val result = AgentBusinessUtr.agentBusinessUtrRead.reads(dbStructure)
      result.get shouldBe AgentBusinessUtr("JARN1234567")
    }

    "be able to read string representation of AgentBusinessUtr" in {
      val restStructure = JsString("JARN1234567")
      val result = AgentBusinessUtr.agentBusinessUtrRead.reads(restStructure)
      result.get shouldBe AgentBusinessUtr("JARN1234567")
    }

  }

  "PsaId reader" should {

    "be able to read obsolete PsaId from Mongo" in {
      val dbStructure = JsObject(Seq("id" -> JsString("A1234567")))
      val result = PsaId.psaIdRead.reads(dbStructure)
      result.get shouldBe PsaId("A1234567")
    }

    "be able to read string representation of PsaId" in {
      val restStructure = JsString("A1234567")
      val result = PsaId.psaIdRead.reads(restStructure)
      result.get shouldBe PsaId("A1234567")
    }

  }

  "PspId reader" should {
    "be able to read obsolete PspId from Mongo" in {
      val dbStructure = JsObject(Seq("id" -> JsString("12345678")))
      val result = PspId.pspIdRead.reads(dbStructure)
      result.get shouldBe PspId("12345678")
    }

    "be able to read string representation of PspId" in {
      val restStructure = JsString("12345678")
      val result = PspId.pspIdRead.reads(restStructure)
      result.get shouldBe PspId("12345678")
    }
  }

  "AwrsUtr reader" should {

    "be able to read obsolete AwrsUtr structure from Mongo" in {
      val dbStructure = JsObject(Seq("utr" -> JsString("XAAW00000123456")))
      val result = AwrsUtr.awrsUtrRead.reads(dbStructure)
      result.get shouldBe AwrsUtr("XAAW00000123456")
    }

    "be able to read string representation of AwrsUtr" in {
      val restStructure = JsString("XAAW00000123456")
      val result = AwrsUtr.awrsUtrRead.reads(restStructure)
      result.get shouldBe AwrsUtr("XAAW00000123456")
    }
  }

  "HmrcObtdsOrg reader" should {

    "be able to read string representation of HmrcObtdsOrg" in {
      val restStructure = JsString("1234567890")
      val result = HmrcObtdsOrg.orgRead.reads(restStructure)
      result.get shouldBe HmrcObtdsOrg("1234567890")
    }
  }
  "HmrcMtdVat reader" should {

    "be able to read string representation of HmrcMtdVat" in {
      val restStructure = JsString("1234567890")
      val result = HmrcMtdVat.orgRead.reads(restStructure)
      result.get shouldBe HmrcMtdVat("1234567890")
    }
  }

}
