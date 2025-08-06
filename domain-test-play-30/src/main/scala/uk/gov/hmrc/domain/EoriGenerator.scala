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
  * Rules for the EORI: https://design.tax.service.gov.uk/hmrc-design-patterns/eori-numbers/
  *
  * Generates an Eori from a random seed. You can use a specific seed to generate an Eori so it is going to be more predictable.
  * @example
  *   EoriGenerator(seed).nextEori.eori
  */
case class EoriGenerator(private val random: Random = new Random) {
  private val countryCode = "GB"
  private val vrn = VrnGenerator(random).nextVrn
  private val branchId = f"${random.nextInt(4)}%03d"

  def nextEori: Eori =
    Eori(s"$countryCode$vrn$branchId")
}

object EoriGenerator {
  def apply(seed: Int): EoriGenerator = EoriGenerator(new Random(seed))
}
