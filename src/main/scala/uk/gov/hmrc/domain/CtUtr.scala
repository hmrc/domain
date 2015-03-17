/*
 * Copyright 2015 HM Revenue & Customs
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

import play.api.libs.json.{Reads, Writes}


case class CtUtr(utr: String) extends TaxIdentifier with SimpleName {
  override lazy val toString = utr
  val name = "ctutr"
  def value = utr
}

object CtUtr {
  implicit val ctUtrWrite: Writes[CtUtr] = new SimpleObjectWrites[CtUtr](_.value)
  implicit val ctUtrRead: Reads[CtUtr] = new SimpleObjectReads[CtUtr]("utr", CtUtr.apply)
}
