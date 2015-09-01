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

package uk.gov.hmrc.domain.nino

import play.api.libs.json.{Reads, Writes}
import uk.gov.hmrc.domain.{SimpleObjectReads, SimpleObjectWrites, TaxIdentifier, SimpleName}

case class NinoWithSuffix(nino: String) extends TaxIdentifier with SimpleName {
  require(NinoWithSuffix.isValid(nino), s"$nino is not a valid nino.")
  override lazy val toString = nino

  def value = nino
  val name = "nino"

  def formatted = value.grouped(2).mkString(" ")
}

object NinoWithSuffix {
  implicit val ninoWrite: Writes[NinoWithSuffix] = new SimpleObjectWrites[NinoWithSuffix](_.value)
  implicit val ninoRead: Reads[NinoWithSuffix] = new SimpleObjectReads[NinoWithSuffix]("nino", NinoWithSuffix.apply)

  private val validNinoFormat = "[[A-Z]&&[^DFIQUV]][[A-Z]&&[^DFIQUVO]] ?\\d{2} ?\\d{2} ?\\d{2} ?[A-Z]{1}"
  private val invalidPrefixes = List("BG", "GB", "NK", "KN", "TN", "NT", "ZZ")

  private def hasValidPrefix(nino: String) = invalidPrefixes.find(nino.startsWith).isEmpty

  def isValid(nino: String) = nino != null && hasValidPrefix(nino) && nino.matches(validNinoFormat)
}
