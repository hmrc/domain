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

import org.scalatest.{Matchers, WordSpec}

class AgentBusinessUtrSpec extends WordSpec with Matchers {

  "validation" should {

    "pass with valid format and check digit" in {
      AgentBusinessUtr.isValid("JARN1234567") should equal(true)
      AgentBusinessUtr.isValid("jarn1234567") should equal(true)
    }

    "fail with an empty string" in {
      AgentBusinessUtr.isValid("") should equal(false)
    }

    "fail when chars 1-4 are not alpha" in {
      AgentBusinessUtr.isValid("1ARN1234567") should equal(false)
      AgentBusinessUtr.isValid("11RN1234567") should equal(false)
      AgentBusinessUtr.isValid("111N1234567") should equal(false)
    }

    "fail when it does not start with 4 alphas" in {
      AgentBusinessUtr.isValid("") should equal(false)
    }

    "fail when the last 7 chars are not digits" in {
      AgentBusinessUtr.isValid("") should equal(false)
    }

    "fail when it does not end in 7 digits" in {
      AgentBusinessUtr.isValid("") should equal(false)
    }

    "fail when chars 2-4 are not ARN" in {
      AgentBusinessUtr.isValid("JBBC1234567") should equal(false)
    }

    "fail when the check character at pos 1 is incorrect" in {
      AgentBusinessUtr.isValid("BARN1234567") should equal(false)
    }

  }

}
