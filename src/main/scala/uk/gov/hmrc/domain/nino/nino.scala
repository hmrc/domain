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

sealed trait ShortOrSuffixedNino extends TaxIdentifier with SimpleName {
  def nino: String
  final def value = nino
  final val name = "nino"
  override lazy val toString = nino

  def formatted = value.grouped(2).mkString(" ")
}

object ShortOrSuffixedNino {
  val invalidPrefixes = List("BG", "GB", "NK", "KN", "TN", "NT", "ZZ")
  def isPrefixValid(nino: String) = invalidPrefixes.find(nino.startsWith).isEmpty
  def apply = parse _
  def parse(nino: String): ShortOrSuffixedNino = {
    //TODO this should be able to use a Try or similar
    if (SuffixedNino.isValid(nino)) SuffixedNino(nino)
    else if (ShortNino.isValid(nino)) ShortNino(nino)
    else throw new IllegalArgumentException(s"$nino is not a valid short or suffixed nino")
  }
}

case class SuffixedNino(nino: String) extends ShortOrSuffixedNino {
  require(SuffixedNino.isValid(nino), s"$nino is not a valid suffixed nino.")

  def shorten = ShortNino(nino.substring(0,nino.length - 1).trim())
}

object SuffixedNino {
  implicit val ninoWithSuffixWrite: Writes[SuffixedNino] = new SimpleObjectWrites[SuffixedNino](_.value)
  implicit val ninoWithSuffixRead: Reads[SuffixedNino] = new SimpleObjectReads[SuffixedNino]("nino", SuffixedNino.apply)

  private val validNinoFormat = ShortNino.validNinoFormat + " ?[A-Z]{1}"

  def isValid(nino: String) = nino != null && ShortOrSuffixedNino.isPrefixValid(nino) && nino.matches(validNinoFormat)

  implicit def toNino(suffixedNino: SuffixedNino): uk.gov.hmrc.domain.Nino = uk.gov.hmrc.domain.Nino(suffixedNino.nino)
}

case class ShortNino(nino: String) extends ShortOrSuffixedNino {
  require(ShortNino.isValid(nino), s"$nino is not a valid short nino.")
}

object ShortNino {
  implicit val ninoWithoutSuffixWrite: Writes[ShortNino] = new SimpleObjectWrites[ShortNino](_.value)
  implicit val ninoWithoutSuffixRead: Reads[ShortNino] = new SimpleObjectReads[ShortNino]("nino", ShortNino.apply)

  private[domain] val validNinoFormat = "[[A-Z]&&[^DFIQUV]][[A-Z]&&[^DFIQUVO]] ?\\d{2} ?\\d{2} ?\\d{2}"

  def isValid(nino: String) = nino != null && ShortOrSuffixedNino.isPrefixValid(nino) && nino.matches(validNinoFormat)
}