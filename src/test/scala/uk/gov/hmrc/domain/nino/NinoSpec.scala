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

import java.io.Serializable

import org.scalatest.{Matchers, WordSpec}

class NinoSpec extends WordSpec with Matchers {

  val validNinoWithoutSpaces = "AB123456C"

  val validNinos = List(
    "without spaces" -> validNinoWithoutSpaces,
    "with spaces" -> "AB 12 34 56 C"
  )

  val startLettersWithOSecond = ('A' to 'Z').map(_ + "O").toList
  val invalidStartLetterCombinations = List('D', 'F', 'I', 'Q', 'U', 'V').combinations(2).map(_.mkString("")).toList
  val invalidPrefixes = startLettersWithOSecond ::: invalidStartLetterCombinations ::: List("BG", "GB", "NK", "KN", "TN", "NT", "ZZ")

  val invalidNinos = List(
    "valid number with leading space" -> s" $validNinoWithoutSpaces",
    "valid number with trailing space" -> s"$validNinoWithoutSpaces ",
    "empty string" -> "",
    "only space" -> "    ",
    "total garbage 1" -> "XXX",
    "total garbage 2" -> "werionownadefwe",
    "total garbage 3" -> "@£%!)(*&^",
    "total garbage 4" -> "123456",
    "only one starting letter and 8 total characters" -> "A123456C",
    "only one starting letter and 9 total characters" -> "A1234567C",
    "three starting letters and 9 total characters" -> "ABC12345C",
    "three starting letters and 10 total characters" -> "ABC123456C",
    "lowercase letters" -> "ab123456c",
    "less than 6 middle digits" -> "AB12345C",
    "more than 6 middle digits" -> "AB1234567C"
  ) ++ invalidPrefixes.map(p => (s"numbers staring with $p", p + "123456C"))

  "The validation of a nino" should {
    for ((description, value) <- validNinos) {
      s"pass with valid number $description" in {
        validateNino(value) should equal(true)
      }
    }
    for ((description, value) <- invalidNinos) {
      s"fail with $description" in {
        validateNino(value) should equal(false)
      }
    }
  }

  "Creating a Nino" should {
    for ((description, value) <- validNinos) {
      s"pass with valid number $description" in {
        NinoWithSuffix(value) should be (a [NinoWithSuffix])
      }
    }
    for ((description, value) <- invalidNinos) {
      s"fail with $description" in {
        an[IllegalArgumentException] should be thrownBy NinoWithSuffix(value)
      }
    }
  }

  "Formatting a Nino" should {
    "produce a formatted nino" in {
      NinoWithSuffix("CS100700A").formatted shouldBe "CS 10 07 00 A"
    }
  }

  def validateNino(nino: String) = NinoWithSuffix.isValid(nino)
}
