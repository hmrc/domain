lazy val domain = (project in file("."))
  .settings(
    scalaVersion := "2.12.15",
    majorVersion := 7,
    isPublicArtefact := true,
    libraryDependencies ++= PlayCrossCompilation.dependencies(
      shared = Seq(
        "org.scalatest"     %% "scalatest"  % "3.0.9"  % "test",
        "org.pegdown"       %  "pegdown"    % "1.6.0"  % "test",
        "org.scalacheck"    %% "scalacheck" % "1.13.5" % "test"
      ),
      play28 = Seq(
        "com.typesafe.play" %% "play-json"  % "2.8.1"
      )
    )
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
  .settings(ScalariformSettings())
  .settings(ScoverageSettings())
  .settings(SilencerSettings())
