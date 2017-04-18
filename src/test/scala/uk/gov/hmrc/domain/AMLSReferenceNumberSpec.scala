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

import org.scalatest.{Matchers, WordSpec}

import scala.util.Random

class AMLSReferenceNumberSpec extends WordSpec with Matchers {
  "AMLSReferenceNumber" should {
    val rand = new Random()

    def randomUCaseLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(rand.nextInt(25)).toString

    def randomDigit = "01234556789".charAt(rand.nextInt(9)).toString

    def randomNumber(digits: Int): String = {
      require(digits >= 1)
      if (digits == 1) {
        randomDigit
      } else {
        randomDigit + randomNumber(digits - 1)
      }
    }

    def buildValidRefNumber() = {
      val partA = randomUCaseLetter
      val partB = randomNumber(6)
      s"X${partA}AW00000${partB}"
    }

    def buildInvalidReferenceNumber(alternativeChar: Char, index: Int) = {
      val valid = buildValidRefNumber()
      valid.substring(0, index) + alternativeChar + valid.substring(index + 1)
    }

    "fail validation" when {
      "input is empty" in {
        an[IllegalArgumentException] should be thrownBy AMLSReferenceNumber("")
      }

      "input does not start with X" in {
        an[IllegalArgumentException] should be thrownBy AMLSReferenceNumber(buildInvalidReferenceNumber('D', 0))
      }

      "second character is a digit" in {
        an[IllegalArgumentException] should be thrownBy AMLSReferenceNumber(buildInvalidReferenceNumber('6', 1))
      }

      "second character is lowercase" in {
        an[IllegalArgumentException] should be thrownBy AMLSReferenceNumber(buildInvalidReferenceNumber('k', 1))
      }

      "the sequence AW00000 does not appear in the correct place" in {
        an[IllegalArgumentException] should be thrownBy AMLSReferenceNumber(buildInvalidReferenceNumber('D', 5))
      }

      "one of the last six characters is not a digit" in {
        an[IllegalArgumentException] should be thrownBy AMLSReferenceNumber(buildInvalidReferenceNumber('D', 12))
      }
    }

    "pass validation" when {
      "input is valid" in {
        AMLSReferenceNumber("XHAW00000678543").utr should be("XHAW00000678543")
      }
    }
  }
}
