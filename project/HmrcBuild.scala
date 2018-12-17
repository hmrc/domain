/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtArtifactory.autoImport.makePublicallyAvailableOnBintray
import uk.gov.hmrc.{SbtArtifactory, SbtAutoBuildPlugin}
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

object HmrcBuild extends Build {

  val appDependencies = Seq(
    Dependencies.Compile.playJson,
    Dependencies.Test.scalaTest,
    Dependencies.Test.pegdown,
    Dependencies.Test.scalaCheck
  )

  lazy val domain = (project in file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
    .settings(
      libraryDependencies ++= appDependencies
    )
    .settings(majorVersion := 5)
    .settings(makePublicallyAvailableOnBintray := true)
}

object Dependencies {

  object Compile {
    val playJson = "com.typesafe.play" %% "play-json" % "2.5.12" % "provided"
  }

  object Test {
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    val pegdown = "org.pegdown" % "pegdown" % "1.6.0" % "test"
    val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.5" % "test"
  }

}
