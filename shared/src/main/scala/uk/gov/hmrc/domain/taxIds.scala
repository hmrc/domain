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

import play.api.libs.json.*

import scala.util.{Failure, Success, Try}

case class TaxIds(values: Set[TaxIds.TaxIdWithName]) {
  import scala.reflect.ClassTag

  require(values.nonEmpty, "TaxIds must have at least one TaxIdentifier")
  require(!TaxIds.hasDuplicates(values.toSeq), "TaxIds cannot have TaxIdentifers with the same name")

  private def as[A <: TaxIds.TaxIdWithName: ClassTag]: Option[A] =
    values.collectFirst { case a: A =>
      a
    }

  lazy val nino: Option[Nino] = as[Nino]
  lazy val saUtr: Option[SaUtr] = as[SaUtr]
  lazy val ctUtr: Option[CtUtr] = as[CtUtr]
  lazy val atedUtr: Option[AtedUtr] = as[AtedUtr]
  lazy val awrsUtr: Option[AwrsUtr] = as[AwrsUtr]
  lazy val vrn: Option[Vrn] = as[Vrn]
  lazy val uar: Option[Uar] = as[Uar]
  lazy val org: Option[Org] = as[Org]
  lazy val agentBusinessUtr: Option[AgentBusinessUtr] = as[AgentBusinessUtr]
  lazy val psaId: Option[PsaId] = as[PsaId]
}

object TaxIds {

  type TaxIdWithName = TaxIdentifier with SimpleName

  def apply(values: TaxIdWithName*): TaxIds = {
    require(!hasDuplicates(values), "TaxIds cannot have TaxIdentifers with the same name")
    TaxIds(values.toSet)
  }

  def hasDuplicates(values: Seq[TaxIdWithName]): Boolean = {
    values.size != values.map(_.name).toSet.size
  }

  def reads(serialisableTaxIds: Set[SerialisableTaxId]): Reads[TaxIds] = (json: JsValue) => {
    val ids: Seq[TaxIdWithName] = serialisableTaxIds.toList.flatMap { taxId =>
      (json \ taxId.taxIdName).asOpt[String].map(taxId.build)
    }
    Try(TaxIds(ids*)) match {
      case Success(taxIds) => JsSuccess(taxIds)
      case Failure(cause)  => JsError(cause.getMessage)
    }
  }

  def writes(serialisableTaxIdsNames: Set[String]): Writes[TaxIds] = (ids: TaxIds) => {
    val values: Set[TaxIdWithName] = ids.values.filterNot(p => !serialisableTaxIdsNames.contains(p.name))
    JsObject(values.map(id => id.name -> Json.toJson(id.value)).toSeq)
  }

  implicit def format(implicit serialisableTaxIds: SerialisableTaxId*): Format[TaxIds] =
    Format(reads(serialisableTaxIds.toSet), writes(serialisableTaxIds.map(_.taxIdName).toSet))

  val defaultSerialisableIds: Seq[SerialisableTaxId] = Seq(
    SerialisableTaxId("nino", Nino.apply),
    SerialisableTaxId("sautr", SaUtr.apply),
    SerialisableTaxId("ctutr", CtUtr.apply),
    SerialisableTaxId("atedutr", AtedUtr.apply),
    SerialisableTaxId("awrsutr", AwrsUtr.apply),
    SerialisableTaxId("vrn", Vrn.apply),
    SerialisableTaxId("uar", Uar.apply),
    SerialisableTaxId("org", Org.apply),
    SerialisableTaxId("agentbusinessutr", AgentBusinessUtr.apply),
    SerialisableTaxId("psaid", PsaId.apply),
    SerialisableTaxId("HMRC-OBTDS-ORG", HmrcObtdsOrg.apply),
    SerialisableTaxId("HMRC-MTD-VAT", HmrcMtdVat.apply),
    SerialisableTaxId("HMRC-MTD-IT", HmrcMtdItsa.apply)
  )
}

case class SerialisableTaxId(taxIdName: String, build: String => TaxIds.TaxIdWithName)
