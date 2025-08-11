ThisBuild / scalaVersion := "3.3.5"
ThisBuild / majorVersion := 13
ThisBuild / isPublicArtefact := true

lazy val domain = (project in file("."))
  .settings(publish / skip := true)
  .aggregate(domainPlay30, domainTestPlay30)

lazy val domainPlay30 = Project("domain-play-30", file("domain-play-30"))
  .settings(
    scalacOptions ++= Seq(
      "-Werror"
    ),
    libraryDependencies ++= LibDependencies(),
    scalafmtOnCompile := true
  )
  .settings(ScoverageSettings())

lazy val domainTestPlay30 = Project("domain-test-play-30", file("domain-test-play-30"))
  .settings(
    scalacOptions ++= Seq(
      "-Werror"
    ),
    libraryDependencies ++= LibDependencies(),
    scalafmtOnCompile := true
  )
  .dependsOn(domainPlay30)
