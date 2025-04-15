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

class Generator(random: Random = new Random) extends Modulus23Check {
  def this(seed: Int) = this(new scala.util.Random(seed))

  def nextNino: Nino = {
    val prefix = Nino.validPrefixes(random.nextInt(Nino.validPrefixes.length))
    val number = random.nextInt(1000000)
    val suffix = Nino.validSuffixes(random.nextInt(Nino.validSuffixes.length))
    Nino(f"$prefix$number%06d$suffix")
  }

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
