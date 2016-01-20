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

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

case class TaxYear(value: String) extends TaxIdentifier {
  require(TaxYear.isValid(value), "TaxYear requires hyphen separated consecutive years with format YYYY-YY")

  override def toString = value

  val startYear = value.split("-")(0)
}

object TaxYear {
  val TaxYearRegex = """^(\d{4})-(\d{2})$"""

  val matchTaxYear: String => Option[Match] = new Regex(TaxYearRegex, "startYear", "endYear") findFirstMatchIn _

  def isValid(taxYearReference: String) = matchTaxYear(taxYearReference) exists {
    r => (r.group("startYear").toInt + 1) % 100 == r.group("endYear").toInt
  }
}
