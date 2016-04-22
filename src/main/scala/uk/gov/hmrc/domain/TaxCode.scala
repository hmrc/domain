/*
 * Copyright 2016 HM Revenue & Customs
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

import play.api.libs.json._

case class TaxCode(code: String) {
  require("^([K]*([1-9][0-9]{0,5}|0)[TLPY]*|BR|NT|D[01])$".r.findFirstMatchIn(code).isDefined)
}

object TaxCodeFormats {
  implicit val taxCodeReads = new TaxCodeReads
  implicit val taxCodeWrites = new TaxCodeWrites

  class TaxCodeReads extends Reads[TaxCode] {
    override def reads(json: JsValue): JsResult[TaxCode] = {
      json match {
        case JsString(code) => {
          try {
            JsSuccess(TaxCode(code))
          } catch {
            case e: IllegalArgumentException => JsError(s"The code $code is not a legal tax code")
          }
        }
        case _ => JsError("Expected a single string")
      }
    }
  }

  class TaxCodeWrites extends Writes[TaxCode] {
    override def writes(o: TaxCode): JsValue = JsString(o.code)
  }
}
