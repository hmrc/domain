/*
 * Copyright 2025 HM Revenue & Customs
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

import scala.util.Random

/** A generator that should be used Only for Testing!
  *
  * Rules for the VRN: https://design.tax.service.gov.uk/hmrc-design-patterns/vat-registration-number/
  *
  * Generates a VRN from a random seed. You can use a specific seed to generate a VRN so it is going to be more predictable.
  *
  * @example
  *   VrnGenerator(seed).nextVrn.vrn
  */
case class VrnGenerator(private val random: Random = new Random) {

  def nextVrn: Vrn = {
    val base = 100000000 // 9 digits
    Vrn(s"${base + random.nextInt(100000000)}")
  }
}

object VrnGenerator {
  def apply(seed: Int): VrnGenerator = VrnGenerator(new Random(seed))
}
