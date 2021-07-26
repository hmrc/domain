/*
 * Copyright 2021 HM Revenue & Customs
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

case class Itsa(itsa: String) extends TaxIdentifier with SimpleName {
  require(Itsa.isValid(itsa), s"$itsa is not a valid itsa.")
  override def toString = itsa

  private val LengthWithoutSuffix: Int = 8

  def value = itsa

  val name = "itsa"

  def formatted = value.grouped(2).mkString(" ")

  def withoutSuffix = value.take(LengthWithoutSuffix)
}

object Itsa extends (String => Itsa) {
  implicit val itsaWrite: Writes[Itsa] = new SimpleObjectWrites[Itsa](_.value)
  implicit val itsaRead: Reads[Itsa] = new SimpleObjectReads[Itsa]("itsa", Itsa.apply)

  // TODO Review the regular expression pattern for ITSA
  private val validItsaFormat = "^[a-zA-Z0-9_]*$"

  def isValid(itsa: String) = itsa != null && itsa.matches(validItsaFormat)
}
