val scala2_12 = "2.12.15"
val scala2_13 = "2.13.8"

val silencerVersion = "1.7.8"

lazy val domain = (project in file("."))
  .settings(
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13),
    majorVersion := 8,
    isPublicArtefact := true,
    libraryDependencies ++= PlayCrossCompilation.dependencies(
      shared = Seq(
        "org.scalatest"     %% "scalatest"  % "3.2.11"  % "test",
        "org.scalatestplus" %% "scalacheck-1-15" % "3.2.11.0" % "test",
        "org.pegdown"       %  "pegdown"    % "1.6.0"  % "test",
        "com.vladsch.flexmark"   %  "flexmark-all"       % "0.62.2" % "test"
      ),
      play28 = Seq(
        "com.typesafe.play" %% "play-json"  % "2.8.2"
      )
    )
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
  .settings(ScalariformSettings())
  .settings(ScoverageSettings())
  .settings(SilencerSettings(silencerVersion))
