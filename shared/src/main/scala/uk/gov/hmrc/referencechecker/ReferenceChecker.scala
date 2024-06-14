/*
 * Copyright 2022 HM Revenue & Customs
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

package uk.gov.hmrc.referencechecker

import scala.util.Try

sealed trait ReferenceChecker {

  val letterToNumber: Map[Char, Int]
  val weights: List[Int]

  val refRegex: String
  def extraChecks: List[String => Boolean] = List(_.matches(refRegex))
  def mainCheck(reference: String, weightedSum: Int): Boolean

  def referenceToValidate(reference: String): String = reference
  def prepareReference(reference: String): String = reference

  implicit class StringImprovements(s: String) {
    def dropCharAtIndex(n: Int) = {
      val (l1, l2) = s.toList splitAt n
      val charList = l1 ::: (l2 drop 1)
      charList.mkString
    }
  }

  implicit class CharImprovements(digitOrLetter: Char) {
    def toNumber: Int = letterToNumber.getOrElse(digitOrLetter, digitOrLetter.asDigit)
  }

  def isValid(reference: String): Boolean = {
    val preparedReference = prepareReference(reference.toUpperCase)

    val mainCheckResult = Try {

      val refToValidate = referenceToValidate(preparedReference)

      val weightedSum = weights.zipWithIndex.collect {
        case (weight, index) if index < refToValidate.length => {
          weight * refToValidate(index).toNumber
        }
      }.sum

      mainCheck(preparedReference, weightedSum)

    } getOrElse false

    extraChecks.foldLeft(mainCheckResult) { (fullCondition, extraCheck) =>
      fullCondition && extraCheck(preparedReference.toUpperCase)
    }

  }
}

trait ModulusReferenceChecker extends ReferenceChecker {

  val remainderLookupTable: List[Char]
  val modulus: Int
  val checkCharIndex: Int

  def mainCheck(reference: String, weightedSum: Int) = {
    remainderLookupTable(weightedSum % modulus) == reference(checkCharIndex)
  }
}

trait DifferenceReferenceChecker extends ReferenceChecker {

  def mainCheck(reference: String, weightedSum: Int) = {
    def checkVatReference(sum: Int, expected: Int): Boolean = {
      var sumLessThan97 = sum
      while (sumLessThan97 > 97) {
        sumLessThan97 -= 97
      }
      val calculatedDigits = 97 - sumLessThan97
      calculatedDigits == expected
    }

    val expected = reference.takeRight(2).toInt
    checkVatReference(weightedSum, expected) || checkVatReference(weightedSum + 55, expected)
  }
}

trait LetterRemainderLookup extends ModulusReferenceChecker {
  val remainderLookupTable = List('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'X', 'J', 'K', 'L', 'M', 'N', 'Y', 'P', 'Q', 'R', 'S', 'T', 'Z', 'V', 'W')
  val modulus = 23
}

trait UtrReferenceChecker extends ModulusReferenceChecker {
  val remainderLookupTable = List('2', '1', '9', '8', '7', '6', '5', '4', '3', '2', '1')
  val modulus = 11
  val checkCharIndex = 0

  val weights = List(6, 7, 8, 9, 10, 5, 4, 3, 2)
  val letterToNumber = Map.empty[Char, Int]
  val refRegex = """^\d{10}$"""

  override def referenceToValidate(reference: String) = reference.tail
}

object SelfAssessmentReferenceChecker extends UtrReferenceChecker {
  override def prepareReference(reference: String) =
    if (reference.startsWith("K")) reference.drop(1)
    else if (reference.endsWith("K")) reference.dropRight(1)
    else reference
}

object CorporationTaxReferenceChecker extends UtrReferenceChecker

object VatReferenceChecker extends DifferenceReferenceChecker {
  val weights = List(8, 7, 6, 5, 4, 3, 2)
  val letterToNumber = Map.empty[Char, Int]
  val refRegex = """^\d{9}$"""
}

object EpayeReferenceChecker extends LetterRemainderLookup {
  val checkCharIndex = 4
  val weights = List(9, 10, 11, 12, 0, 8, 7, 6, 5, 4, 3, 2, 1)
  val letterToNumber = Map('A' -> 33, 'B' -> 34, 'C' -> 35, 'D' -> 36, 'E' -> 37, 'F' -> 38, 'G' -> 39, 'H' -> 40, 'J' -> 42, 'K' -> 43, 'L' -> 44, 'M' -> 45,
    'N' -> 46, 'P' -> 48, 'Q' -> 49, 'R' -> 50, 'S' -> 51, 'T' -> 52, 'U' -> 56, 'V' -> 54, 'W' -> 55, 'X' -> 41, 'Y' -> 47, 'Z' -> 53)
  val refRegex = """^\d{3}P[A-Z]\d{6}[A-Z_0-9]{2}$"""
  override def extraChecks = super.extraChecks :+
    ((ref: String) => if (ref.startsWith("961") && ref(12) == 'X') ref(5) == '0' else true) :+
    ((ref: String) => if (ref.startsWith("961") && ref(12) != 'X') ref.matches("""^961P[A-Z]\d{8}$""") else true)
}

object OtherTaxReferenceChecker extends LetterRemainderLookup {
  val checkCharIndex = 1
  val weights = List(0, 0, 9, 10, 11, 12, 13, 8, 7, 6, 5, 4, 3, 2, 1)
  val letterToNumber = Map('A' -> 33, 'B' -> 34, 'C' -> 35, 'D' -> 36, 'E' -> 37, 'F' -> 38, 'G' -> 39, 'H' -> 40, 'I' -> 41, 'J' -> 42, 'K' -> 43, 'L' -> 44, 'M' -> 45,
    'N' -> 46, 'O' -> 47, 'P' -> 48, 'Q' -> 49, 'R' -> 50, 'S' -> 51, 'T' -> 52, 'U' -> 53, 'V' -> 54, 'W' -> 55, 'X' -> 56, 'Y' -> 57, 'Z' -> 58)
  val refRegex = """^X[A-Z](([0-9A-Z]{13})|([0-9A-Z]\d{11}))$"""
}

object SdltReferenceChecker extends LetterRemainderLookup {
  val checkCharIndex = 10
  val weights = List(6, 7, 8, 9, 10, 5, 4, 3, 2)
  val letterToNumber =
    Map('A' -> 0, 'B' -> 1, 'C' -> 2, 'D' -> 3, 'E' -> 4, 'F' -> 5, 'G' -> 6, 'H' -> 7, 'X' -> 8, 'J' -> 9, 'K' -> 10,
      'L' -> 11, 'M' -> 12, 'N' -> 13, 'Y' -> 14, 'P' -> 15, 'Q' -> 16, 'R' -> 17, 'S' -> 18, 'T' -> 19, 'Z' -> 20, 'V' -> 21, 'W' -> 22)
  val refRegex = """^\d{9}M[A-Z]$"""
}
