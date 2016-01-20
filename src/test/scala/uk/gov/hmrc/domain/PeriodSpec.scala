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

import org.scalatest.{ShouldMatchers, WordSpec}

class PeriodSpec extends WordSpec with ShouldMatchers {

  "isValid" should {
    "return true for valid period" in {
      val periodValues = 1 to 12 map (_.toString)

      periodValues foreach {
        Period.isValid(_) shouldBe true
      }
    }

    "return false for invalid period" in {
      val invalidPeriodValues = List("-1", "0", "13", "notANumber", "-12", "1.5")

      invalidPeriodValues.foreach(Period.isValid(_) shouldBe false)
    }
  }

  "Creating a Period from a string" should {
    "set correct period value" in {
      Period("1").value shouldBe "1"
    }

    "throw an InvalidArgumentException for invalid input" in {
      val invalidPeriodValues = List("-1", "0", "13", "notANumber", "-12", "1.5")

      invalidPeriodValues.foreach(
        an[IllegalArgumentException] should be thrownBy Period(_)
      )
    }
  }

  "toString" should {
    "stringify a Period object" in {
      Period("1").toString shouldBe "1"
    }
  }
}
