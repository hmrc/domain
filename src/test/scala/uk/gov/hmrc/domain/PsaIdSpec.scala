/*
 * Copyright 2020 HM Revenue & Customs
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

import org.scalatest.{WordSpec, Matchers}

class PsaIdSpec extends WordSpec with Matchers {

  "validation" must {

    "fail with an empty string" in {
      PsaId.isValid("") should equal(false)
    }

    "fail when the first character is not an alpha" in {
      PsaId.isValid("12345678") should equal(false)
    }

    "fail when the last 7 characters are not numeric" in {
      PsaId.isValid("aa234567") should equal(false)
      PsaId.isValid("a1a34567") should equal(false)
      PsaId.isValid("a12a4567") should equal(false)
      PsaId.isValid("a123a567") should equal(false)
      PsaId.isValid("a1234a67") should equal(false)
      PsaId.isValid("a12345a7") should equal(false)
      PsaId.isValid("a123456a") should equal(false)
    }

    "fail when longer than 8 characters" in {
      PsaId.isValid("a1234567a") should equal(false)
      PsaId.isValid("a12345678") should equal(false)
    }

    "pass when 1 alpha followed by 7 characters" in {
      PsaId.isValid("a1234567") should equal(true)
      PsaId.isValid("B1234567") should equal(true)
    }
  }

}
