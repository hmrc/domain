/*
 * Copyright 2016 HM Revenue & Customs
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

trait Modulus23Check extends ModulusCheck {

  override protected val checkString = "ABCDEFGHXJKLMNYPQRSTZVW"
  override protected val mod = 23
  override protected val weights = List(9, 10, 11, 12, 13, 8, 7, 6, 5, 4, 3, 2, 1)

}
