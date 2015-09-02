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

import org.scalatest.{FreeSpec, Matchers, WordSpec}

class NinoSpec extends FreeSpec with Matchers {

  val validNino = "AB123456"
  val validSuffix = "C"

  val alwaysInvalidNinos = List(
    "empty string" -> "",
    "only space" -> "    ",
    "total garbage 1" -> "XXX",
    "total garbage 2" -> "werionownadefwe",
    "total garbage 3" -> "@£%!)(*&^",
    "total garbage 4" -> "123456"
  )

  val startLettersWithOSecond = ('A' to 'Z').map(_ + "O").toList
  val invalidStartLetterCombinations = List('D', 'F', 'I', 'Q', 'U', 'V').combinations(2).map(_.mkString("")).toList
  val invalidPrefixes = startLettersWithOSecond ::: invalidStartLetterCombinations ::: List("BG", "GB", "NK", "KN", "TN", "NT", "ZZ")

  "For SuffixedNino" - {
    val validNinoWithSuffix = validNino + validSuffix
    val validNinos = List(
      "without spaces" -> validNinoWithSuffix,
      "with spaces" -> "AB 12 34 56 C"
    )

    val invalidNinos =
      List(
        "valid number without a suffix" -> validNino,
        "valid number with leading space" -> s" $validNinoWithSuffix",
        "valid number with trailing space" -> s"$validNinoWithSuffix ",
        "only one starting letter and 8 total characters" -> "A123456C",
        "only one starting letter and 9 total characters" -> "A1234567C",
        "three starting letters and 9 total characters" -> "ABC12345C",
        "three starting letters and 10 total characters" -> "ABC123456C",
        "lowercase letters" -> "ab123456c",
        "less than 6 middle digits" -> "AB12345C",
        "more than 6 middle digits" -> "AB1234567C"
      ) ++
        alwaysInvalidNinos ++
        invalidPrefixes.map(p => (s"numbers staring with $p", p + "123456C"))

    "The validation should" - {
      for ((description, value) <- validNinos) {
        s"pass with valid number $description: '$value'" in {
          SuffixedNino.isValid(value) should equal(true)
        }
      }
      for ((description, value) <- invalidNinos) {
        s"fail with $description: '$value'" in {
          SuffixedNino.isValid(value) should equal(false)
        }
      }
    }

    "Creating should" - {
      for ((description, value) <- validNinos) {
        s"pass with valid number $description: '$value'" in {
          SuffixedNino(value) should be(a[SuffixedNino])
        }
      }
      for ((description, value) <- invalidNinos) {
        s"fail with $description: '$value'" in {
          an[IllegalArgumentException] should be thrownBy SuffixedNino(value)
        }
      }
    }

    "a valid instance should be able to be" - {
      "formatted" in {
        SuffixedNino("CS100700A").formatted shouldBe "CS 10 07 00 A"
      }
      "shortened without spaces" in {
        SuffixedNino("CS100700A").shorten shouldBe ShortNino("CS100700")
      }
      "shortened with spaces" in {
        SuffixedNino("CS 10 07 00 A").shorten shouldBe ShortNino("CS 10 07 00")
      }
    }

    "Parsing as a ShortOrSuffixedNino should" - {
      for ((description, value) <- validNinos) {
        s"extract to SuffixedNino for valid number $description: '$value'" in {
          ShortOrSuffixedNino.parse(value) should (be (a [ShortOrSuffixedNino]) and be (a [SuffixedNino]))
        }
      }
      for ((description, value) <- invalidNinos.filter { case(_,v) => v != validNino }) {
        s"fail with $description: '$value'" in {
          an[IllegalArgumentException] should be thrownBy ShortOrSuffixedNino.parse(value)
        }
      }
    }
  }

  "For ShortNino" - {
    val validNinos = List(
      "without spaces" -> validNino,
      "with spaces" -> "AB 12 34 56"
    )
    val invalidNinos =
      List(
        "valid number with a suffix" -> (validNino + validSuffix),
        "valid number with leading space" -> s" $validNino",
        "valid number with trailing space" -> s"$validNino ",
        "only one starting letter and 7 total characters" -> "A123456",
        "only one starting letter and 8 total characters" -> "A1234567",
        "three starting letters and 8 total characters" -> "ABC12345",
        "three starting letters and 9 total characters" -> "ABC123456",
        "lowercase letters" -> "ab123456",
        "less than 6 middle digits" -> "AB12345",
        "more than 6 middle digits" -> "AB1234567"
      ) ++
        alwaysInvalidNinos ++
        invalidPrefixes.map(p => (s"numbers staring with $p", p + "123456C"))

    "The validation should" - {
      for ((description, value) <- validNinos) {
        s"pass with valid number $description: '$value'" in {
          ShortNino.isValid(value) should equal(true)
        }
      }
      for ((description, value) <- invalidNinos) {
        s"fail with $description: '$value'" in {
          ShortNino.isValid(value) should equal(false)
        }
      }
    }

    "Creating should" - {
      for ((description, value) <- validNinos) {
        s"pass with valid number $description: '$value'" in {
          ShortNino(value) should be(a[ShortNino])
        }
      }
      for ((description, value) <- invalidNinos) {
        s"fail with $description: '$value'" in {
          an[IllegalArgumentException] should be thrownBy ShortNino(value)
        }
      }
    }

    "Formatting should" - {
      "produce a formatted nino" in {
        SuffixedNino("CS100700A").formatted shouldBe "CS 10 07 00 A"
      }
    }

    "Parsing as a ShortOrSuffixedNino should" - {
      for ((description, value) <- validNinos) {
        s"extract to ShortNino a valid number $description: '$value'" in {
          ShortOrSuffixedNino.parse(value) should (be (a [ShortOrSuffixedNino]) and be (a [ShortNino]))
        }
      }
      for ((description, value) <- invalidNinos.filter { case(_,v) => v != (validNino + validSuffix) }) {
        s"fail with $description: '$value'" in {
          an[IllegalArgumentException] should be thrownBy ShortOrSuffixedNino.parse(value)
        }
      }
    }
  }
}
