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

class Random(random: scala.util.Random = new scala.util.Random) {
  def this(seed: Int) = this(new scala.util.Random(seed))

  private def generateCharacterByRange(start: Char, end: Char): String =
    (start + random.nextInt(end - start)).toChar.toString

  private def generatePrefixCharacter(invalidCharacters: List[String]): String = {
    val character = generateCharacterByRange('A', 'Z')
    if (invalidCharacters.contains(character)) generatePrefixCharacter(invalidCharacters)
    else character
  }

  private def generatePrefix: String = {
    val prefix = generatePrefixCharacter(Nino.invalidFirstPrefixCharacters) +
      generatePrefixCharacter(Nino.invalidSecordPrefixCharacters)
    if (Nino.invalidPrefixes.contains(prefix)) generatePrefix
    else prefix
  }

  def nextNino: Nino = {
    val prefix = generatePrefix
    val number = random.nextInt(1000000)
    val suffix = generateCharacterByRange('A', 'D')
    Nino(f"$prefix$number%06d$suffix")
  }
}
