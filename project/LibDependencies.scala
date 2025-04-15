import sbt.*

object LibDependencies {

  private val compile = Seq(
    "org.playframework" %% "play" % "3.0.7"
  )

  private val test = Seq(
    "org.scalatest"        %% "scalatest"       % "3.2.19",
    "org.scalatestplus"    %% "scalacheck-1-17" % "3.2.18.0",
    "com.vladsch.flexmark" % "flexmark-all"     % "0.64.8"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}