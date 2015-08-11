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

import scala.Left
import scala.Right

import play.api.data.FormError
import play.api.data.format.Formatter

object NinoFormatter {

  def nino(messageKeyBuilder: Option[(String, String) => String] = None,
           removeInternalSpaces: Option[Boolean] = None) =
    new Formatter[Nino] {
      override def unbind(key: String, value: Nino) = Map(key -> value.nino)
      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Nino] =
        data.get(key) match {
          case None =>
            Left(List(FormError(key, buildMessageKey(messageKeyBuilder, key, "required"))))
          case Some(userEnteredNino) =>
            val modifiedNino = removeInternalSpaces match {
              case Some(true) => userEnteredNino.replaceAll(" ", "").toUpperCase()
              case _          => userEnteredNino.trim.toUpperCase()
            }
            modifiedNino match {
              case emptyNino if (emptyNino.isEmpty()) =>
                Left(List(FormError(key, buildMessageKey(messageKeyBuilder, key, "required"))))
              case nino if (Nino.isValid(nino)) =>
                Right(Nino(nino))
              case _ =>
                Left(List(FormError(key, buildMessageKey(messageKeyBuilder, key, "invalid"))))
            }
        }
    }

  private def defaultMessageKeyBuilder(key: String, error: String): String = {
    s"error.${error}"
  }

  private def buildMessageKey(messageKeyBuilder: Option[(String, String) => String], key: String, error: String): String = {
    messageKeyBuilder.getOrElse[(String, String) => String](defaultMessageKeyBuilder).apply(key, error)
  }
}
