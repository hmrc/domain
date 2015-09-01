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

trait Nino extends TaxIdentifier with SimpleName {
  def nino: String
  final def value = nino
  final val name = "nino"
  override lazy val toString = nino

  def formatted = value.grouped(2).mkString(" ")
}

object Nino {
  val invalidPrefixes = List("BG", "GB", "NK", "KN", "TN", "NT", "ZZ")
  def isPrefixValid(nino: String) = invalidPrefixes.find(nino.startsWith).isEmpty
}

case class NinoWithSuffix(nino: String) extends Nino {
  require(NinoWithSuffix.isValid(nino), s"$nino is not a valid nino.")
}

object NinoWithSuffix {
  implicit val ninoWithSuffixWrite: Writes[NinoWithSuffix] = new SimpleObjectWrites[NinoWithSuffix](_.value)
  implicit val ninoWithSuffixRead: Reads[NinoWithSuffix] = new SimpleObjectReads[NinoWithSuffix]("nino", NinoWithSuffix.apply)

  private val validNinoFormat = "[[A-Z]&&[^DFIQUV]][[A-Z]&&[^DFIQUVO]] ?\\d{2} ?\\d{2} ?\\d{2} ?[A-Z]{1}"

  def isValid(nino: String) = nino != null && Nino.isPrefixValid(nino) && nino.matches(validNinoFormat)
}

case class NinoWithoutSuffix(nino: String) extends Nino {
  require(NinoWithoutSuffix.isValid(nino), s"$nino is not a valid nino.")
}

object NinoWithoutSuffix {
  implicit val ninoWithoutSuffixWrite: Writes[NinoWithoutSuffix] = new SimpleObjectWrites[NinoWithoutSuffix](_.value)
  implicit val ninoWithoutSuffixRead: Reads[NinoWithoutSuffix] = new SimpleObjectReads[NinoWithoutSuffix]("nino", NinoWithoutSuffix.apply)

  private val validNinoFormat = "[[A-Z]&&[^DFIQUV]][[A-Z]&&[^DFIQUVO]] ?\\d{2} ?\\d{2} ?\\d{2}"

  def isValid(nino: String) = nino != null && Nino.isPrefixValid(nino) && nino.matches(validNinoFormat)
}