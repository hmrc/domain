/*
 * Copyright 2015 HMRC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.domain

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsError, JsObject, JsString}

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

  }
}
