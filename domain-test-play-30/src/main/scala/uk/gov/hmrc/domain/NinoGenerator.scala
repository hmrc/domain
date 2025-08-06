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
  * Rules for the Nino: https://en.wikipedia.org/wiki/National_Insurance_number#Format
  *
  * Generates a Nino from a random seed. You can use a specific seed to generate a Nino so it is going to be more predictable.
  * @example
  *   NinoGenerator(seed).nextNino.nino
  */
case class NinoGenerator(private val random: Random = new Random) extends Modulus23Check {

  def nextNino: Nino = {
    val prefix = Nino.validPrefixes(random.nextInt(Nino.validPrefixes.length))
    val number = random.nextInt(1000000)
    val suffix = Nino.validSuffixes(random.nextInt(Nino.validSuffixes.length))
    Nino(f"$prefix$number%06d$suffix")
  }
}

object NinoGenerator {
  def apply(seed: Int): NinoGenerator = NinoGenerator(Random(seed))
}
