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

import scala.util.Try

sealed trait ShortOrSuffixedNino extends TaxIdentifier with SimpleName {
  def nino: String
  final def value = nino
  final val name = "nino"
  override lazy val toString = nino

  def formatted = value.grouped(2).mkString(" ")
}

object ShortOrSuffixedNino {
  implicit val shortOrSuffixedNinoWrite: Writes[ShortOrSuffixedNino] = new SimpleObjectWrites[ShortOrSuffixedNino](_.value)
  implicit val shortOrSuffixedNinoRead: Reads[ShortOrSuffixedNino] = new SimpleObjectReads[ShortOrSuffixedNino]("nino", ShortOrSuffixedNino.apply)

  def parse(nino: String): Option[ShortOrSuffixedNino] = SuffixedNino.parse(nino) orElse ShortNino.parse(nino)
  def apply(nino: String) = parse(nino) getOrElse (throw new IllegalArgumentException(s"$nino is not a valid short or suffixed nino"))
}

case class SuffixedNino(nino: String) extends ShortOrSuffixedNino {
  require(SuffixedNino.isValid(nino), s"$nino is not a valid suffixed nino.")

  def shorten = ShortNino(nino.substring(0,nino.length - 1).trim())
}

trait NinoValidation {
  private val invalidPrefixes = List("BG", "GB", "NK", "KN", "TN", "NT", "ZZ")
  private val formatRegex = "[[A-Z]&&[^DFIQUV]][[A-Z]&&[^DFIQUVO]] ?\\d{2} ?\\d{2} ?\\d{2}"
  private def isPrefixValid(nino: String) = invalidPrefixes.find(nino.startsWith).isEmpty

  private[domain] def suffixRegex: String

  def isValid(nino: String) = nino != null && isPrefixValid(nino) && nino.matches(formatRegex + suffixRegex)
}

object SuffixedNino extends NinoValidation {
  implicit val ninoWithSuffixWrite: Writes[SuffixedNino] = new SimpleObjectWrites[SuffixedNino](_.value)
  implicit val ninoWithSuffixRead: Reads[SuffixedNino] = new SimpleObjectReads[SuffixedNino]("nino", SuffixedNino.apply)

  @deprecated(message = "Nino has been replaced by ShortNino, SuffixedNino and ShortOrSuffixedNino", since = "1/09/2015")
  val ninoWrite = ninoWithSuffixWrite
  @deprecated(message = "Nino has been replaced by ShortNino, SuffixedNino and ShortOrSuffixedNino", since = "1/09/2015")
  val ninoRead = ninoWithSuffixRead

  override val suffixRegex = " ?[A-Z]{1}"

  def parse(nino: String) = Try(SuffixedNino(nino)).toOption
}

case class ShortNino(nino: String) extends ShortOrSuffixedNino {
  require(ShortNino.isValid(nino), s"$nino is not a valid short nino.")
}

object ShortNino extends NinoValidation {
  implicit val ninoWithoutSuffixWrite: Writes[ShortNino] = new SimpleObjectWrites[ShortNino](_.value)
  implicit val ninoWithoutSuffixRead: Reads[ShortNino] = new SimpleObjectReads[ShortNino]("nino", ShortNino.apply)

  override val suffixRegex = ""

  def parse(nino: String) = Try(ShortNino(nino)).toOption
}