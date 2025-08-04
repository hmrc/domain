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

import scala.util.Random

/** A generator that should be used Only for Testing!
  *
  * Generates an SaUtr from a random seed.
  *
  * You can use a specific seed to generate an SaUtr so it is going to be more predictable.
  *
  * @example
  *   SaUtrGenerator(seed).nextSaUtr.utr
  */
case class SaUtrGenerator(private val random: Random = new Random) extends Modulus11Check {

  def nextSaUtr: SaUtr = {
    val suffix = f"${random.nextInt(100000)}%09d"
    val checkCharacter = calculateCheckCharacter(suffix)
    SaUtr(s"$checkCharacter$suffix")
  }
}

object SaUtrGenerator {
  def apply(seed: Int): SaUtrGenerator = SaUtrGenerator(Random(seed))
}
