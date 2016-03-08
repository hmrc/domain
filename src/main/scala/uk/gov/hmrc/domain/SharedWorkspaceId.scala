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

import play.api.libs.json.{Reads, Writes}

case class SharedWorkspaceId(id: String) extends TaxIdentifier with SimpleName {
  require(SharedWorkspaceId.isValid(id))
  override def toString = id
  val name = "sharedworkspaceid"
  def value = id
}

object SharedWorkspaceId  extends (String => SharedWorkspaceId) {
  implicit val sharedWorkspaceIdWrite: Writes[SharedWorkspaceId] = new SimpleObjectWrites[SharedWorkspaceId](_.value)
  implicit val sharedWorkspaceIdRead: Reads[SharedWorkspaceId] = new SimpleObjectReads[SharedWorkspaceId]("id", SharedWorkspaceId.apply)

  private val validFormat = "^[a-zA-Z]\\d{7}$"

  def isValid(id: String) = !id.isEmpty
}
