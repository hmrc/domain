/*
 * Copyright 2016 HM Revenue & Customs
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

case class AtedUtr(utr: String) extends TaxIdentifier with SimpleName {
  require(AtedUtr.isValid(utr))
  override def toString = utr
  val name = "atedutr"
  def value = utr
}

object AtedUtr extends Modulus23Check with (String => AtedUtr) {
  implicit val atedUtrWrite: Writes[AtedUtr] = new SimpleObjectWrites[AtedUtr](_.value)
  implicit val atedUtrRead: Reads[AtedUtr] = new SimpleObjectReads[AtedUtr]("utr", AtedUtr.apply)

  private val validFormats = Seq(
    """^[Xx][a-zA-Z]\d{2}00000\d{6}$""",
    """^[Xx][A-Z]AT00000\d{6}$"""
  )

  def isValid(utr: String) =
    validFormats.exists(utr.matches) &&
      isCheckCorrect(utr.toUpperCase, 1)
}
