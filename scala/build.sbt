ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "performance-orm-scala",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.xerial" % "sqlite-jdbc" % "3.42.0.0",
      "com.typesafe.slick" %% "slick" % "3.4.1"
    ),
    // Fat JARを作る場合に使用 (sbt-assembly)
    mainClass in assembly := Some("Main")
  )
