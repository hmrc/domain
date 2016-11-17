/*
 * Copyright 2016 HM Revenue & Customs
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

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}


class P800ReferenceCheckerSpec extends WordSpec with TableDrivenPropertyChecks with Matchers {

  val invalidReferences = Table(
    ("Reference", "Problem"),
    ("", "is empty"),
    ("SJ12312DP8002016", "has NINO with 5 digits"),
    ("SJ123123P8002016", "has NINO without last letter"),
    ("AC123123DP8002016", "has NINO with invalid first letters"),
    ("SJ123123D2016", "doesn't have P800"),
    ("SJ123123DP800", "doesn't have a year"),
    ("SJ123123DP800201g", "has invalid year")
  )

  "P800 checker" should {
    "return true for valid P800 reference string" in {
      P800ReferenceChecker.isValid("SJ123123DP8002016") shouldEqual true
    }


    forAll(invalidReferences) { (reference, problem) =>
      s"return false for string '$reference' that $problem" in {
        P800ReferenceChecker.isValid(reference) shouldEqual false
      }
    }
  }
}
