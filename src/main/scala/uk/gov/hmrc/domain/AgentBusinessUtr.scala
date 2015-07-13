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

case class AgentBusinessUtr(utr: String) extends TaxIdentifier with SimpleName {
  override lazy val toString = utr
  val name = "agentbusinessutr"
  def value = utr
}

object AgentBusinessUtr {
  implicit val agentBusinessUtrWrite: Writes[AgentBusinessUtr] = new SimpleObjectWrites[AgentBusinessUtr](_.value)
  implicit val agentBusinessUtrRead: Reads[AgentBusinessUtr] = new SimpleObjectReads[AgentBusinessUtr]("utr", AgentBusinessUtr.apply)

  private val validFormat = "^[a-zA-Z][Aa][Rr][Nn]\\d{7}$"

  def isValid(utr: String): Boolean = !utr.isEmpty && utr.matches(validFormat) && isCheckCorrect(utr.toUpperCase)

  private def isCheckCorrect(utr: String): Boolean = utr.head == getCheckCharacter(utr)

  private val checkString = "ABCDEFGHXJKLMNYPQRSTZVW"
  private val mod = 23
  private val weights = List(9, 10, 11, 12, 13, 8, 7, 6, 5, 4)

  private def getCheckCharacter(utr: String): Char = {
    var sum = weights.zipWithIndex.collect {
      case (weight, index)  => {
        if(index < 5) {
          weight * (utr.charAt(index+1).asDigit + mod)
        } else {
          weight * utr.charAt(index+1).asDigit
        }
      }
    }.sum
    checkString.charAt(sum % mod)
  }

}