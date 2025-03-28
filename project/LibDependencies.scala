import sbt._

object LibDependencies {
  val play28 = Seq(
    "com.typesafe.play" %% "play" % "2.8.22"
  )

  val play29 = Seq(
    "com.typesafe.play" %% s"play" % "2.9.3"
  )

  val play30 = Seq(
    "org.playframework" %% s"play" % "3.0.7"
  )

  val test = Seq(
    "org.scalatest"        %% "scalatest"       % "3.2.18",
    "org.scalatestplus"    %% "scalacheck-1-17" % "3.2.18.0",
    "com.vladsch.flexmark" % "flexmark-all"     % "0.64.8"
  ).map(_ % Test)
}