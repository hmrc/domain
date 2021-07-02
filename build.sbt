lazy val domain = (project in file("."))
  .settings(
    scalaVersion := "2.12.12",
    majorVersion := 6,
    isPublicArtefact := true,
    libraryDependencies ++= PlayCrossCompilation.dependencies(
      shared = Seq(
        "org.scalatest"     %% "scalatest"  % "3.0.1"  % "test",
        "org.pegdown"       %  "pegdown"    % "1.6.0"  % "test",
        "org.scalacheck"    %% "scalacheck" % "1.13.5" % "test"
      ),
      play26 = Seq(
        "com.typesafe.play" %% "play-json"  % "2.6.13"
      ),
      play27 = Seq(
        "com.typesafe.play" %% "play-json"  % "2.7.4"
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
