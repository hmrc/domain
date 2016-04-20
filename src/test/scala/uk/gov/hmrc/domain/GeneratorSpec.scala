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

package uk.gov.hmrc.domain

import org.scalacheck.Prop
import org.scalatest.WordSpec
import org.scalatest.prop.Checkers

class GeneratorSpec extends WordSpec with Checkers {

  "Nino Generation" should {
    "generate valid NINOs for all random seeds" in {
      check(Prop.forAll { (seed: Int) => Nino.isValid(new Generator(seed).nextNino.nino) })
    }
  }

  "AtedUtr Generation" should {

    "generate valid AtedUtrs for all random seeds" in {
      check(Prop.forAll { (seed: Int) => AtedUtr.isValid(new Generator(seed).nextAtedUtr.utr)})
    }

    "generate a batch of unique AtedUtrs" in {
      val atedUtrs = new Generator().atedUtrBatch(100000)
      assert(atedUtrs.distinct.length == atedUtrs.length)
    }

  }
}
