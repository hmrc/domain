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

import scala.collection.mutable.ListBuffer
import scala.util.Random

/** A generator that should be used Only for Testing!
  *
  * Rules for the AtedUtr: https://design.tax.service.gov.uk/hmrc-design-patterns/unique-taxpayer-reference/
  *
  * Generates an AtedUtr from a random seed. You can use a specific seed to generate an AtedUtr so it is going to be more predictable.
  * @example
  *   AtedUtrGenerator(seed).nextAtedUtr.utr
  */
case class AtedUtrGenerator(private val random: Random = new Random) extends Modulus23Check {

  def atedUtrBatch(amountToGenerate: Int): List[AtedUtr] = {
    require(amountToGenerate <= 900000,
            throw new IllegalArgumentException("Can't generate more than 9000000 unique AtedUtrs, specify a smaller value for amount")
           )
    val atedUtrs: ListBuffer[AtedUtr] = ListBuffer()
    var start = 100000
    for (a <- 0 until amountToGenerate) {
      val stringToWeight = s"AT00000$start"
      val checkChar = calculateCheckCharacter(stringToWeight)
      atedUtrs.++=(Seq(AtedUtr(s"X$checkChar$stringToWeight")))
      start = start + 1
    }
    atedUtrs.toList
  }

  def nextAtedUtr: AtedUtr = {
    val suffix = f"${random.nextInt(9) + 1}${random.nextInt(100000)}%05d"
    val weighting = s"AT00000$suffix"
    val checkCharacter = calculateCheckCharacter(weighting)
    AtedUtr(f"X${checkCharacter}AT00000$suffix")
  }
}

object AtedUtrGenerator {
  def apply(seed: Int): AtedUtrGenerator = AtedUtrGenerator(Random(seed))
}
