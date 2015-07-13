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

trait CheckCharacter {

  protected val checkString = "ABCDEFGHXJKLMNYPQRSTZVW"
  protected val mod = 23
  protected val weights = List(9, 10, 11, 12, 13, 8, 7, 6, 5, 4, 3, 2, 1)

  protected def isCheckCorrect(utr: String, checkPosition: Int, offset: Int): Boolean = utr.charAt(checkPosition) == getCheckCharacter(utr, offset)

  private def getCheckCharacter(utr: String, offset: Int): Char = {
    val sum = weights.zipWithIndex.collect {
      case (weight, index) if (index + offset < utr.length) => {
        val char = utr.charAt(index + offset)
        if (char.isLetter) {
          weight * (char.asDigit + mod)
        } else {
          weight * char.asDigit
        }
      }
    }.sum

    checkString.charAt(sum % mod)
  }

}
