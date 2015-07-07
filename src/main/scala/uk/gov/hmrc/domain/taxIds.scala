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

import play.api.libs.json._

import scala.util.{Failure, Success, Try}

case class TaxIds(private val valuesAsList: TaxIds.TaxIdWithName*) {
  import scala.reflect._

  require(valuesAsList.nonEmpty, "TaxIds must have at least one TaxIdentifier")
  require(!TaxIds.hasDuplicates(valuesAsList), "TaxIds cannot have TaxIdentifers with the same name")

  val values = valuesAsList.toSet

  private def as[A <: TaxIds.TaxIdWithName](implicit tag: ClassTag[A]): Option[A] = values.find(id => tag.runtimeClass.isInstance(id)) map (id => tag.runtimeClass.cast(id).asInstanceOf[A])

  lazy val nino = as[Nino]
  lazy val saUtr = as[SaUtr]
  lazy val ctUtr = as[CtUtr]
  lazy val atedUtr = as[AtedUtr]
  lazy val vrn = as[Vrn]
  lazy val uar = as[Uar]
  lazy val org = as[Org]
}

object TaxIds {

  type TaxIdWithName = TaxIdentifier with SimpleName

  def hasDuplicates(values: Seq[TaxIdWithName]): Boolean = {
    values.size != values.map(_ name).toSet.size
  }

  def reads(serialisableTaxIds: Set[SerialisableTaxId]): Reads[TaxIds] = new Reads[TaxIds] {
    override def reads(json: JsValue): JsResult[TaxIds] = {
      val ids = serialisableTaxIds.toList.map { taxId =>
        (json \ taxId.taxIdName).asOpt[String] map (taxId build)
      }.flatten[TaxIdWithName]
      Try(TaxIds(ids: _*)) match {
        case Success(taxIds) => JsSuccess(taxIds)
        case Failure(cause) => JsError(cause.getMessage)
      }
    }
  }

  def writes(serialisableTaxIdsNames: Set[String]): Writes[TaxIds] = new Writes[TaxIds] {
    override def writes(ids: TaxIds) = {
      val values: Set[TaxIdWithName] = ids.values.filterNot(p => !serialisableTaxIdsNames.contains(p.name))
      JsObject(values.map { id => id.name -> Json.toJson(id.value)}.toSeq)
    }
  }

  implicit def format(implicit serialisableTaxIds: SerialisableTaxId*): Format[TaxIds] = Format(reads(serialisableTaxIds.toSet), writes(serialisableTaxIds.map(_.taxIdName).toSet))

  val defaultSerialisableIds = Seq(
    SerialisableTaxId("nino",  Nino.apply),
    SerialisableTaxId("sautr", SaUtr.apply),
    SerialisableTaxId("ctutr", CtUtr.apply),
    SerialisableTaxId("atedutr", AtedUtr.apply),
    SerialisableTaxId("vrn",   Vrn.apply),
    SerialisableTaxId("uar",   Uar.apply),
    SerialisableTaxId("org",   Org.apply)
  )
}

case class SerialisableTaxId(taxIdName: String, build: (String) => TaxIds.TaxIdWithName)
