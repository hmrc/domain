/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.domain

import org.scalatest.{ShouldMatchers, WordSpec}
import play.api.libs.json.{JsError, JsNumber, JsString, JsSuccess}

class TaxCodeFormatsSpec extends WordSpec with ShouldMatchers {
  import TaxCodeFormats._

  "TaxCode reads" should {
    "correctly read legal string" in {
      taxCodeReads.reads(JsString("K100")) shouldBe JsSuccess(TaxCode("K100"))
    }

    "return JsError if the json is not a string" in {
      taxCodeReads.reads(JsNumber(2)) shouldBe JsError("Expected a single string")
    }

    "return JsError if the string is not a legal taxcode" in {
      taxCodeReads.reads(JsString("foo")) shouldBe JsError("The code foo is not a legal tax code")
    }
  }

  "TaxCode writes" should {
    "correctly write a legal taxcode" in {
      taxCodeWrites.writes(TaxCode("K100")) shouldBe JsString("K100")
    }
  }
}
