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

package uk.gov.hmrc.domain

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class TaxCodeSpec extends AnyWordSpec {
  "Tax code" should {
    val legalCodes = Seq("0T", "D0", "D1", "123456L", "100P", "100Y", "K500", "BR", "NT")

    val illegalCodes = Seq("1234567", "1A3456P", "123456H", "123456A", "NI", "69 L", " 69L", "K0500", "6O9L", "OT",
      "69LM1", "69LW1", "500K", "474Lwk1/mth1", "474L/X", "474L/1", "D")

    legalCodes.foreach { code =>
      s"construct a legal tax code for $code" in {
        val taxCode = new TaxCode(code)
        taxCode.code shouldBe code
      }
    }

    illegalCodes.foreach { code =>
      s"throw an exception for the code $code" in {
        intercept[IllegalArgumentException] {
          new TaxCode(code)
        }
      }
    }
  }
}
