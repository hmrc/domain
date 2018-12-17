/*
 * Copyright 2018 HM Revenue & Customs
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

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpecLike}

class ModulusCheckerSpec extends WordSpecLike with Matchers with GeneratorDrivenPropertyChecks {

  "The SA reference checker" should {

    val ValidReferences = Seq("2234567890K", "2108834503K", "1097172564K", "2234567890", "K2234567890", "1097172564k", "k1097172564")
    val InvalidReferences = Seq("", "123456", "1234567890K", "inv@lid123K", "inv@lid123K", "1nv@l-d~&*!", "1234567890E", "12345678905", "1097172564KZ")

    "return true for a valid reference" in {
      checkAllValid(SelfAssessmentReferenceChecker, ValidReferences)
    }

    "return false for an invalid reference" in {
      checkAllInvalid(SelfAssessmentReferenceChecker, InvalidReferences)
    }

    "return true for correctly generated references" in {
      val checkSum = Gen.listOfN(9, Gen.chooseNum(0, 9))

      val weights = List(6, 7, 8, 9, 10, 5, 4, 3, 2)
      val remainderLookupTable = List(2,1,9,8,7,6,5,4,3,2,1)

      forAll(checkSum) { (ints: Seq[Int]) =>
        val remainder = (ints, weights).zipped.map {
          case (s, i) => s * i
        }.sum % 11
        val firstNumber: Int = remainderLookupTable(remainder)
        SelfAssessmentReferenceChecker.isValid(firstNumber + ints.mkString + "K") shouldBe true
      }
    }

  }

  "CT checksum validator" should {
    val ValidReferences = Seq("1097172564", "2108834503", "2234567890")
    val InvalidReferences = Seq("", "123456", "5550000621", "inv@lid123")


    "return true for a valid reference" in {
      checkAllValid(CorporationTaxReferenceChecker, ValidReferences)
    }

    "return false for an invalid reference" in {
      checkAllInvalid(CorporationTaxReferenceChecker, InvalidReferences)
    }


    "return true for correctly generated references" in {
      val checkSum = Gen.listOfN(9, Gen.chooseNum(0, 9))

      val weights = List(6, 7, 8, 9, 10, 5, 4, 3, 2)
      val remainderLookupTable = List(2, 1, 9, 8, 7, 6, 5, 4, 3, 2, 1)

      forAll(checkSum) { (ints: Seq[Int]) =>
        val remainder = (ints, weights).zipped.map {
          case (s, i) => s * i
        }.sum % 11
        val firstNumber: Int = remainderLookupTable(remainder)
        CorporationTaxReferenceChecker.isValid(firstNumber + ints.mkString) should be (true)
      }
    }

  }

  "The VAT reference checker" should {

    val ValidReferences = Seq("101747696", "101747641", "000000097")
    val InvalidReferences = Seq("", "123456", "123456789", "123abc789")

    "return true for a valid reference" in {
      checkAllValid(VatReferenceChecker, ValidReferences)
    }

    "return false for an invalid reference" in {
      checkAllInvalid(VatReferenceChecker, InvalidReferences)
    }

    val vatWeights = List(8, 7, 6, 5, 4, 3, 2)

    "return true for correctly generated references for modulo 97" in {
      val checkSum = Gen.listOfN(7, Gen.chooseNum(0, 9))

      forAll(checkSum) { (ints: Seq[Int]) =>
        whenever(ints.size == 7) {
          var sums = (ints, vatWeights).zipped.map {
            case (s, i) => s * i
          }.sum

          while (sums > 97) sums -= 97

          val reference = ints.mkString + "%02d".format(97 - sums)
          VatReferenceChecker.isValid(reference) shouldBe true
        }
      }
    }

    "return true for correctly generated references for modulo 97 55" in {
      val checkSum = Gen.listOfN(7, Gen.chooseNum(0, 9))

      forAll(checkSum) { (ints: Seq[Int]) =>
        whenever(ints.size == 7) {
          var sums = (ints, vatWeights).zipped.map {
            case (s, i) => s * i
          }.sum + 55

          while (sums > 97) sums -= 97

          val reference = ints.mkString + "%02d".format(97 - sums)
          VatReferenceChecker.isValid(reference) shouldBe true
        }
      }
    }
  }

  "The ePAYE reference checker" should {

    val ValidReferences = Seq("123PW12345678", "450PE00923437", "120PY19064375", "450PA0096437K", "853PW0076584X", "846PS00928342", "961PA0045782X", "961PF10305739", "123PH45678900")
    val InvalidReferences = Seq("", "1234", "000PL00000000", "120PA12697495", "846PT00568772", "820PB0091254X", "961PI004578OQ", "123PW12345678Z", "8465L04697138", "120PW003179212324", "120PW0024", "123XH4!@£900", "961PA1145782X", "961PL004578FQ")

    "return true for a valid reference" in {
      checkAllValid(EpayeReferenceChecker, ValidReferences)
    }

    "return false for an invalid reference" in {
      checkAllInvalid(EpayeReferenceChecker, InvalidReferences)
    }
  }

  "The Other taxes reference checker" should {

    val Valid14DigitReferences = Seq("XA012345678901", "XDS98712398712", "XDS98712398712", "XAP74589243785", "XE123456789012", "XM123995577846", "XM003425669813")
    val Valid15DigitReferences = Seq("XP00CATINTHEHAT", "XH1234567890123", "XQ0938588127545", "XK0046537877231", "XRgreeneggs4ham", "XM0yertleturtle")

    val Invalid14DigitReferences = Seq("XF093624679222", "XW135792468123", "XFG03589343785", "inv@lid12A3456", "12345678901234", "X1234567890123", "XA1234567890AA")
    val Invalid15DigitReferences = Seq("X12345678901233", "XJ9876543210123", "XAP74589243785Z", "XP1234567890120", "XM7846942270421", "X23456789012345")
    val InvalidReferences = Seq("", "123456", "123456789A123")

    "return true for a valid 14 digit reference" in {
      checkAllValid(OtherTaxReferenceChecker, Valid14DigitReferences)
    }

    "return true for a valid 15 digit reference" in {
      checkAllValid(OtherTaxReferenceChecker, Valid15DigitReferences)
    }

    "return false for an invalid 14 digit reference" in {
      checkAllInvalid(OtherTaxReferenceChecker, Invalid14DigitReferences)
    }

    "return false for an invalid 15 digit reference" in {
      checkAllInvalid(OtherTaxReferenceChecker, Invalid15DigitReferences)
    }

    "return false for an reference" in {
      checkAllInvalid(OtherTaxReferenceChecker, InvalidReferences)
    }
  }

  "The SDLT reference checker" should {

    val ValidReferences = Seq("123456789MA", "918273645MV", "576682142MP", "586110842MR", "143245939MZ", "345726781MA","335626481MS", "680686481MW","430622221MH","222222222MQ", "123456789MA")
    val InvalidReferences = Seq("", "12345", "233795664MB", "946725624MQ", "848785828MB","151617181MC","273647567MJ", "632926282MQ","246485202MQ","426442202MQ","486848282MA","222222222MX", "A23456789MA", "1A3456789MA", "12A456789MA", "123A56789MA", "1234A6789MA", "12345A789MA", "123456A89MA", "1234567A9MA", "12345678AMA", "576682142MPZ", "123456789MAAAAAAA", "123456789DA", "123456789M2")

    "return true for a valid reference" in {
      checkAllValid(SdltReferenceChecker, ValidReferences)
    }

    "return false for an invalid reference" in {
      checkAllInvalid(SdltReferenceChecker, InvalidReferences)
    }

  }

  def checkAllValid(checker: ReferenceChecker, refs: Seq[String]) = {
    val invalidRefs = refs.filterNot(checker.isValid)

    withClue("Invalid references found: " + invalidRefs + "\n") {
      invalidRefs should be (empty)
    }
  }

  def checkAllInvalid(checker: ReferenceChecker, refs: Seq[String]) = {
    val validRefs = refs.filter(checker.isValid)

    withClue("Valid references found: " + validRefs + "\n") {
      validRefs should be (empty)
    }
  }
}
