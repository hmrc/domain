/*
 * Copyright 2018 HM Revenue & Customs
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

class SaUtrGenerator(random: Random = new Random) extends Modulus11Check {
  def this(seed: Int) = this(new scala.util.Random(seed))

  def nextSaUtr: SaUtr = {
    val suffix = f"${random.nextInt(100000)}%09d"
    val checkCharacter  = calculateCheckCharacter(suffix)
    SaUtr(s"$checkCharacter$suffix")
  }
}
