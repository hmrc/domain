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

import scala.util.Try

case class Period(value: String) extends TaxIdentifier {
  require(Period.isValid(value), "Period must be an integer between 1 and 12")

  override def toString = value
}

object Period {
  val isInteger = (value: String) => Try(value.toInt).isSuccess
  val between1And12 = (value: Int) => value >= 1 && value <= 12

  def isValid(value: String) = {
    isInteger(value) && between1And12(value.toInt)
  }
}
