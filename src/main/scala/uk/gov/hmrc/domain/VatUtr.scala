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

case class VatUtr(utr: String) extends TaxIdentifier with SimpleName {
  override def toString = value
  override def value: String = utr
  override val name: String = "VatUtr"
}

object VatUtr {
  implicit val vatUtrWrite: Writes[VatUtr] = new SimpleObjectWrites[VatUtr](_.value)
  implicit val vatUtrRead: Reads[VatUtr] = new SimpleObjectReads[VatUtr]("vatUtr", VatUtr.apply)
}
