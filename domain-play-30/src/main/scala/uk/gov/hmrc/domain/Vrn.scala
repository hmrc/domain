/*
 * Copyright 2025 HM Revenue & Customs
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

case class Vrn(vrn: String) extends TaxIdentifier with SimpleName {
  require(Vrn.isValid(vrn), s"$vrn is not a valid vrn.")
  override def toString: String = vrn
  val name = "vrn"
  def value: String = vrn
}

object Vrn extends (String => Vrn) {
  implicit val vrnWrite: Writes[Vrn] = new SimpleObjectWrites[Vrn](_.value)
  implicit val vrnRead: Reads[Vrn] = new SimpleObjectReads[Vrn]("vrn", Vrn.apply)

  private val validFormat = "^[0-9]{9}$"
  private val obsoleteMongoFormat = "^[0-9]{5}$"
  def isValid(id: String): Boolean = id.nonEmpty && (id.matches(validFormat) || id.matches(obsoleteMongoFormat))
}
