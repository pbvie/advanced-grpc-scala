// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `cookbook-grpc-scala-client` =
  project
      .in(file("client"))
      .enablePlugins(AutomateHeaderPlugin, GitVersioning)
      .dependsOn(`cookbook-grpc-scala-protocol`)
      .settings(settings)
      .settings(
        libraryDependencies ++= Seq(
          library.cats,
          library.log4j2Api,
          library.log4j2Core,
          library.log4j2Scala,
          library.monix,
          library.pureConfig,
          library.scalaCheck % Test,
          library.scalaTest  % Test
        ),
        addCommandAlias("run-greeter-client", ";cookbook-grpc-scala-client/runMain io.ontherocks.cookbookgrpc.client.greeter.GreeterApp")
      )

lazy val `cookbook-grpc-scala-protocol` =
  project
      .in(file("protocol"))
      .enablePlugins(AutomateHeaderPlugin, GitVersioning)
      .settings(settings)
      .settings(scalaPbSettings)
      .settings(
        libraryDependencies ++= Seq(
          library.grpcNetty,
          library.scalaPbRuntime,
          library.scalaPbRuntimeGrpc,
          library.scalaCheck % Test,
          library.scalaTest  % Test
        )
      )

lazy val `cookbook-grpc-scala-service` =
  project
      .in(file("service"))
      .enablePlugins(AutomateHeaderPlugin, GitVersioning, JavaAppPackaging, AshScriptPlugin)
      .dependsOn(`cookbook-grpc-scala-protocol`)
      .settings(settings)
      .settings(dockerSettings)
      .settings(
        libraryDependencies ++= Seq(
          library.cats,
          library.log4j2Api,
          library.log4j2Core,
          library.log4j2Scala,
          library.monix,
          library.pureConfig,
          library.scalaCheck % Test,
          library.scalaTest  % Test
        ),
        mainClass in Compile := Some("io.ontherocks.cbgrpc.service.Main"),
        version in Docker    := "0.0.1",
        addCommandAlias("run-services", ";cookbook-grpc-scala-service/run"
        )
      )

lazy val `cookbook-grpc-scala` =
  project
    .in(file("."))
    .aggregate(
      `cookbook-grpc-scala-client`,
      `cookbook-grpc-scala-protocol`,
      `cookbook-grpc-scala-service`
      )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    import scalapb.compiler.Version.grpcJavaVersion
    import scalapb.compiler.Version.scalapbVersion
    object Version {
      val cats        = "1.1.0"
      val log4j2      = "2.11.0"
      val log4j2Scala = "11.0"
      val monix       = "2.3.3"
      val pureconfig  = "0.9.1"
      val scalaCheck  = "1.14.0"
      val scalaTest   = "3.0.5"
    }
    val cats               = "org.typelevel"             %% "cats-core"             % Version.cats
    val grpcNetty          = "io.grpc"                    % "grpc-netty"            % grpcJavaVersion
    val log4j2Api          = "org.apache.logging.log4j"   % "log4j-api"             % Version.log4j2
    val log4j2Core         = "org.apache.logging.log4j"   % "log4j-core"            % Version.log4j2 % Runtime
    val log4j2Scala        = "org.apache.logging.log4j"   % "log4j-api-scala_2.12"  % Version.log4j2Scala
    val monix              = "io.monix"                  %% "monix"                 % Version.monix
    val pureConfig         = "com.github.pureconfig"     %% "pureconfig"            % Version.pureconfig
    val scalaCheck         = "org.scalacheck"            %% "scalacheck"            % Version.scalaCheck
    val scalaPbRuntime     = "com.thesamet.scalapb"      %% "scalapb-runtime"       % scalapbVersion % "protobuf"
    val scalaPbRuntimeGrpc = "com.thesamet.scalapb"      %% "scalapb-runtime-grpc"  % scalapbVersion
    val scalaTest          = "org.scalatest"             %% "scalatest"             % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  gitSettings ++
  scalafmtSettings

lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    // scalaVersion := "2.12.4",
    organization := "io.ontherocks",
    organizationName := "Petra Bierleutgeb",
    startYear := Some(2017),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-Xfatal-warnings",
      "-Ypartial-unification",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused-import",
      "-Ywarn-unused",
      "-encoding", "UTF-8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value)
)

lazy val dockerSettings =
  Seq(
    dockerBaseImage    := "openjdk:8-jdk-alpine",
    dockerUpdateLatest := true
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

lazy val scalaPbSettings = Seq(
  PB.targets in Compile := Seq(
    scalapb.gen() -> (sourceManaged in Compile).value
  )
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
  )
