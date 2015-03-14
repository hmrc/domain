import sbt._
import Keys._

object HmrcBuild extends Build {

  import uk.gov.hmrc.PublishingSettings._
  import uk.gov.hmrc.DefaultBuildSettings
  import DefaultBuildSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}

  val appVersion = "2.6.0-SNAPSHOT"

  val appDependencies = Seq(
    Dependencies.Compile.playJson,
    Dependencies.Test.scalaTest,
    Dependencies.Test.pegdown
  )

  lazy val domain = (project in file("."))
    .settings(version := appVersion)
    .settings(scalaSettings : _*)
    .settings(defaultSettings() : _*)
    .settings(
      targetJvm := "jvm-1.7",
      shellPrompt := ShellPrompt(appVersion),
      libraryDependencies ++= appDependencies,
      crossScalaVersions := Seq("2.11.6"),
      resolvers := Seq(
        Opts.resolver.sonatypeReleases,
        Opts.resolver.sonatypeSnapshots,
        "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/",
        "typesafe-snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
      )
    )
    .settings(publishAllArtefacts: _*)
    .settings(SbtBuildInfo(): _*)
    .settings(SonatypeBuild(): _*)
    .settings(Headers(): _ *)
}

object Dependencies {

  object Compile {
    val playJson = "com.typesafe.play" %% "play-json" % "2.3.8" % "provided"
  }

  sealed abstract class Test(scope: String) {
    val scalaTest = "org.scalatest" %% "scalatest" % "2.2.4" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.4.2" % scope
  }

  object Test extends Test("test")

  object IntegrationTest extends Test("it")

}

object SonatypeBuild {

  import xerial.sbt.Sonatype._

  def apply() = {
    sonatypeSettings ++ Seq(
      pomExtra := {
        <url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          </license>
        </licenses>
          <scm>
            <connection>scm:git@github.tools.tax.service.gov.uk:HMRC/domain.git</connection>
            <developerConnection>scm:git@github.tools.tax.service.gov.uk:HMRC/domain.git</developerConnection>
            <url>git@github.tools.tax.service.gov.uk:HMRC/domain.git</url>
          </scm>
          <developers>
            <developer>
              <id>duncancrawford</id>
              <name>Duncan Crawford</name>
              <url>http://www.equalexperts.com</url>
            </developer>
            <developer>
              <id>jakobgrunig</id>
              <name>Jakob Grunig</name>
              <url>http://www.equalexperts.com</url>
            </developer>
            <developer>
              <id>xnejp03</id>
              <name>Petr Nejedly</name>
              <url>http://www.equalexperts.com</url>
            </developer>
            <developer>
              <id>vaughansharman</id>
              <name>Vaughan Sharman</name>
              <url>http://www.equalexperts.com</url>
            </developer>
            <developer>
              <id>davesammut</id>
              <name>Dave Sammut</name>
              <url>http://www.equalexperts.com</url>
            </developer>
            <developer>
              <id>howyp</id>
              <name>Howard Perrin</name>
              <url>http://www.zuhlke.co.uk</url>
            </developer>
          </developers>
      }
    )
  }
}

object Headers {
  import de.heikoseeberger.sbtheader.SbtHeader.autoImport._
  def apply() = Seq(
    headers := Map(
      "scala" ->(
        HeaderPattern.cStyleBlockComment,
        """|/*
           | * Copyright 2015 HM Revenue & Customs
           | *
           | * Licensed under the Apache License, Version 2.0 (the "License");
           | * you may not use this file except in compliance with the License.
           | * You may obtain a copy of the License at
           | *
           | *   http://www.apache.org/licenses/LICENSE-2.0
           | *
           | * Unless required by applicable law or agreed to in writing, software
           | * distributed under the License is distributed on an "AS IS" BASIS,
           | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
           | * See the License for the specific language governing permissions and
           | * limitations under the License.
           | */
           |
           |""".stripMargin
        )
    ),
    (compile in Compile) <<= (compile in Compile) dependsOn (createHeaders in Compile),
    (compile in Test) <<= (compile in Test) dependsOn (createHeaders in Test)
  )
}