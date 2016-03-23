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

case class AMLSReferenceNumber (utr : String)  extends TaxIdentifier with SimpleName {
  require(AMLSReferenceNumber.isValid(utr))

  override def value: String = utr
  override val name: String = "amlsutr"
}

object AMLSReferenceNumber {
  private def isValid(utr : String) : Boolean  = {
    utr.matches("^X[A-Z]AW00000[0-9]{6}$")
  }
}
