/*
 * Copyright 2021 HM Revenue & Customs
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

class ItsaSpec extends WordSpec with Matchers {
  import Itsa.isValid

  "The validation of an itsa" should {
    "pass with valid alphanumerics" in {
      isValid("AB123456C") shouldBe true
    }
    // TODO add more test cases here ...
  }
}
