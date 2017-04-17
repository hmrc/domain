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

import org.scalatest._

class TaxYearSpec extends WordSpec with ShouldMatchers {

  "isValid" should {
    "return true for valid taxYear" in {
      val validTaxYearValues = List("2014-15", "2000-01", "2099-00")
      validTaxYearValues foreach {
        TaxYear.isValid(_) shouldBe true
      }
    }

    "return false for invalid taxYear" in {
      val invalidTaxYearValues = List("2014-16", "14-15", "2014", "14", "2014-13", "2014-2015")
      invalidTaxYearValues foreach {
        TaxYear.isValid(_) shouldBe false
      }
    }
  }

  "Creating a TaxYear" should {
    "set correct value for startYear" in {
      TaxYear("2015-16").startYear shouldBe "2015"
    }

    "throw an InvalidArgumentException for invalid taxYear value" in {
      val invalidTaxYearValues = List("2014-16", "14-15", "2014", "14", "2014-13", "2014-2015")

      invalidTaxYearValues.foreach(
        an[IllegalArgumentException] should be thrownBy TaxYear(_)
      )
    }
  }

  "toString" should {
    "stringify a TaxYear object" in {
      TaxYear("2015-16").toString shouldBe "2015-16"
    }
  }
}
