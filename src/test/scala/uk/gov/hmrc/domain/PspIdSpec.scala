/*
 * Copyright 2019 HM Revenue & Customs
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

class PspIdSpec extends WordSpec with Matchers {

  "validation" must {

    "fail with an empty string" in {
      PspId.isValid("") should equal(false)
    }

    "fail when there are non-numeric characters" in {
      PspId.isValid("aa234567") should equal(false)
      PspId.isValid("a1a34567") should equal(false)
      PspId.isValid("a12a4567") should equal(false)
      PspId.isValid("a123a567") should equal(false)
      PspId.isValid("a1234a67") should equal(false)
      PspId.isValid("a12345a7") should equal(false)
      PspId.isValid("a123456a") should equal(false)
    }

    "fail when longer than 8 characters" in {
      PspId.isValid("123456789") should equal(false)
    }

    "pass when 8 digits" in {
      PspId.isValid("12345678") should equal(true)
    }
  }
}
