/*
 * Copyright 2022 HM Revenue & Customs
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

case class PsaId(id: String) extends TaxIdentifier with SimpleName {
  require(PsaId.isValid(id))
  override def toString = id
  val name = "psaid"
  def value = id
}

object PsaId extends (String => PsaId) {
  implicit val psaIdWrite: Writes[PsaId] = new SimpleObjectWrites[PsaId](_.value)
  implicit val psaIdRead: Reads[PsaId] = new SimpleObjectReads[PsaId]("id", PsaId.apply)

  private val validFormat = "^[a-zA-Z]\\d{7}$"

  def isValid(id: String) = !id.isEmpty && id.matches(validFormat)
}
