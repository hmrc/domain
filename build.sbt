lazy val domain = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json"  % "2.6.13",
      "org.scalatest"     %% "scalatest"  % "3.0.1"  % "test",
      "org.pegdown"       %  "pegdown"    % "1.6.0"  % "test",
      "org.scalacheck"    %% "scalacheck" % "1.13.5" % "test"
    ),
    crossScalaVersions := List("2.11.12", "2.12.8"),
    majorVersion := 6,
    makePublicallyAvailableOnBintray := true
  )