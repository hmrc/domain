/*
 * Copyright 2015 HM Revenue & Customs
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

import play.api.libs.json.{Reads, Writes}
import uk.gov.hmrc.domain.{SimpleObjectReads, SimpleObjectWrites, SimpleName, TaxIdentifier}

case class PsaUtr(utr: String) extends TaxIdentifier with SimpleName {
  require(PsaUtr.isValid(utr))
  override lazy val toString = utr
  val name = "psautr"
  def value = utr
}

object PsaUtr {
  implicit val psaUtrWrite: Writes[PsaUtr] = new SimpleObjectWrites[PsaUtr](_.value)
  implicit val psaUtrRead: Reads[PsaUtr] = new SimpleObjectReads[PsaUtr]("utr", PsaUtr.apply)

  private val validFormat = "^[a-zA-Z]\\d{7}$"

  def isValid(utr: String) = !utr.isEmpty && utr.matches(validFormat)
}
