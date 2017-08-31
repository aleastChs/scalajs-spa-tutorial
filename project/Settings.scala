import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

/**
  * Application settings. Configure the build for your application here.
  * You normally don't have to touch the actual build definition after this.
  */
object Settings {
  /** The name of your application */
  val name = "scalajs-spa"

  /** The version of your application */
  val version = "1.1.5"

  /** Options for the scala compiler */
  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding", "utf8",
    "-language:implicitConversions",
    "-language:postfixOps"
  )

  /** Declare global dependency versions here to avoid mismatches in multi part dependencies */
  object versions {
    lazy val scala = "2.12.3"
    lazy val scalaDom = "0.9.2"
    lazy val scalajsReact = "1.1.0"
    lazy val scalaCSS = "0.5.3"
    lazy val log4js = "1.4.10"
    lazy val autowire = "0.2.6"
    lazy val booPickle = "1.2.6"
    lazy val diode = "1.1.2"
    lazy val uTest = "0.4.7"

    lazy val react = "15.5.4"
    lazy val jQuery = "1.11.1"
    lazy val bootstrap = "3.3.6"
    lazy val chartjs = "2.1.3"

    lazy val scalajsScripts = "1.1.1"

    lazy val scalaRX = "0.3.2"
    lazy val d3 = "0.3.4"
    lazy val scalaTest = "3.0.1"
    lazy val playJson = "2.6.3"
    lazy val derivedCodecs = "4.0.0"
    lazy val scalaTime = "2.0.0-M12"
    lazy val parser = "1.0.5"
  }

  /**
    * These dependencies are shared between JS and JVM projects
    * the special %%% function selects the correct version for each project
    */
  val sharedDependencies = Def.setting(Seq(
    "com.lihaoyi" %%% "autowire" % versions.autowire,
    "io.suzaku" %%% "boopickle" % versions.booPickle
  ))

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(Seq(
    "com.vmunier" %% "scalajs-scripts" % versions.scalajsScripts,
    "org.webjars" % "font-awesome" % "4.3.0-1" % Provided,
    "org.webjars" % "bootstrap" % versions.bootstrap % Provided,
    "com.lihaoyi" %% "utest" % versions.uTest % Test,
    "org.scalatest" %%% "scalatest" % versions.scalaTest % "test"
  ))

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % versions.scalajsReact,
    "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
    "com.github.japgolly.scalacss" %%% "core" % versions.scalaCSS,
    "com.github.japgolly.scalacss" %%% "ext-react" % versions.scalaCSS,
    "io.suzaku" %%% "diode" % versions.diode,
    "io.suzaku" %%% "diode-react" % versions.diode,

    "org.scala-lang.modules" %%% "scala-parser-combinators" % versions.parser,
    "com.lihaoyi" %%% "scalarx" % versions.scalaRX,
    "org.singlespaced" %%% "scalajs-d3" % versions.d3,
    "org.scala-js" %%% "scalajs-dom" % versions.scalaDom,
    "io.github.cquiroz" %%% "scala-java-time" % versions.scalaTime,

    "com.typesafe.play" %%% "play-json" % versions.playJson,
    "org.julienrf" %%% "play-json-derived-codecs" % versions.derivedCodecs,

    "org.scalatest" %%% "scalatest" % versions.scalaTest % "test",
    "com.lihaoyi" %%% "utest" % versions.uTest % Test
  ))

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting(Seq(
    "org.webjars" % "log4javascript" % versions.log4js / "js/log4javascript_uncompressed.js" minified "js/log4javascript.js"
  ))
}