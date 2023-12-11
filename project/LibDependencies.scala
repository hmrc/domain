import sbt._

object LibDependencies {
  val play28 = Seq(
    "com.typesafe.play" %% "play" % "2.8.21"
  )

  val play29 = Seq(
    "com.typesafe.play" %% s"play" % "2.9.0"
  )

  val play30 = Seq(
    "org.playframework" %% s"play" % "3.0.0"
  )

  val test = Seq(
    "org.scalatest"         %% "scalatest"      % "3.2.11",
    "org.scalatestplus"    %% "scalacheck-1-15" % "3.2.11.0",
    "org.pegdown"          % "pegdown"          % "1.6.0",
    "com.vladsch.flexmark" % "flexmark-all"     % "0.62.2"
  ).map(_ % Test)
}