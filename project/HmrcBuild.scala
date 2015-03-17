import sbt._
import Keys._

object HmrcBuild extends Build {

  import uk.gov.hmrc.PublishingSettings._
  import uk.gov.hmrc.DefaultBuildSettings
  import DefaultBuildSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}

  val appVersion = "2.7.0-SNAPSHOT"

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
    .settings(POMMetadata(): _*)
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

object POMMetadata {

  def apply() = {
      pomExtra :=
        <url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          </license>
        </licenses>
          <scm>
            <connection>scm:git@github.com:hmrc/domain.git</connection>
            <developerConnection>scm:git@github.com:hmrc/domain.git</developerConnection>
            <url>git@github.com:hmrc/domain.git</url>
          </scm>
          <developers>
            <developer>
              <id>duncancrawford</id>
              <name>Duncan Crawford</name>
              <url>http://www.equalexperts.com</url>
            </developer>
            <developer>
              <id>xnejp03</id>
              <name>Petr Nejedly</name>
              <url>http://www.equalexperts.com</url>
            </developer>
            <developer>
              <id>howyp</id>
              <name>Howard Perrin</name>
              <url>http://www.zuhlke.co.uk</url>
            </developer>
          </developers>
  }
}

object Headers {

  import de.heikoseeberger.sbtheader.SbtHeader.autoImport._
  import de.heikoseeberger.sbtheader.license.Apache2_0

  def apply() = Seq(
    headers := Map("scala" -> Apache2_0("2015", "HM Revenue & Customs")),
    (compile in Compile) <<= (compile in Compile) dependsOn (createHeaders in Compile),
    (compile in Test) <<= (compile in Test) dependsOn (createHeaders in Test)
  )
}
