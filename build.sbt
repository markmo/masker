lazy val commonScalacOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

val akkaVersion = "2.4.16"
val akkaHttpVersion = "10.0.1"

lazy val akkaMainDependencies = Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)

lazy val akkaHttpDependencies = Seq(
  "de.heikoseeberger" %% "akka-sse" % "2.0.0",
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.11.0"
)

lazy val commonSettings = Seq(
  name := "masker",
  version := "1.0",
  scalaVersion := "2.12.1",
  organization in ThisBuild := "com.telstra.daas.ai.harold",
  incOptions := incOptions.value.withLogRecompileOnMacro(false),
  scalacOptions ++= commonScalacOptions,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.9.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  ),
  fork in test := true,
  parallelExecution in Test := false,
  scalacOptions in (Compile, doc) := (scalacOptions in (Compile, doc)).value.filter(_ != "-Xfatal-warnings")
)

lazy val commonJvmSettings = Seq(
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
)

lazy val masker = project
  .in(file("."))
  .settings(commonSettings)
  .settings(commonJvmSettings)
  .settings(libraryDependencies ++= akkaMainDependencies ++ akkaHttpDependencies)
  .enablePlugins(JavaAppPackaging)
