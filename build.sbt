lazy val domain = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    libraryDependencies ++= PlayCrossCompilation.dependencies(
      shared = Seq(
        "org.scalatest"     %% "scalatest"  % "3.0.1"  % "test",
        "org.pegdown"       %  "pegdown"    % "1.6.0"  % "test",
        "org.scalacheck"    %% "scalacheck" % "1.13.5" % "test"
      ),
      play25 = Seq(
        "com.typesafe.play" %% "play-json"  % "2.5.19"
      ),
      play26 = Seq(
        "com.typesafe.play" %% "play-json"  % "2.6.13"
      ),
      play27 = Seq(
        "com.typesafe.play" %% "play-json"  % "2.7.4"
      )
    ),
    crossScalaVersions := List("2.11.12", "2.12.8"),
    majorVersion := 5,
    makePublicallyAvailableOnBintray := true
  ).settings(PlayCrossCompilation.playCrossCompilationSettings)
