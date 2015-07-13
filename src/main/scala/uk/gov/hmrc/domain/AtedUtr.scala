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

case class AtedUtr(utr: String) extends TaxIdentifier with SimpleName {
  override lazy val toString = utr
  val name = "atedutr"
  def value = utr
}

object AtedUtr {
  implicit val atedUtrWrite: Writes[AtedUtr] = new SimpleObjectWrites[AtedUtr](_.value)
  implicit val atedUtrRead: Reads[AtedUtr] = new SimpleObjectReads[AtedUtr]("utr", AtedUtr.apply)

  private val validFormat = "^[Xx][a-zA-Z]\\d{2}00000\\d{6}$"

  def isValid(utr: String) = !utr.isEmpty && utr.matches(validFormat) && isCheckCorrect(utr.toUpperCase)

  private def isCheckCorrect(utr: String): Boolean = utr.charAt(1) == getCheckCharacter(utr)

  private val checkString = "ABCDEFGHXJKLMNYPQRSTZVW"
  private val mod = 23
  private val weights = List(9, 10, 11, 12, 13, 8, 7, 6, 5, 4, 3, 2, 1)

  private def getCheckCharacter(utr: String): Char = {
    val sum = weights.zipWithIndex.collect {
      case (weight, index)  => weight * utr.charAt(index+2).asDigit
    }.sum
    checkString.charAt(sum % mod)
  }
}
