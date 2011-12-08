import sbt._
import Keys._

object SuperEliderBuild extends Build {
  override lazy val settings = super.settings ++ Seq(
    organization := "net.tixxit",
    version := "0.1",
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

  lazy val root = Project(id = "superelider", base = file(".")) aggregate (plugin, lib)

  lazy val plugin = Project(id = "superelider-plugin", base = file("plugin")) settings (
    name := "Superelider Compiler Plugin",
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
  ) dependsOn (lib)

  lazy val lib = Project(id = "superelider-annotation", base = file("annotation")) settings (
    name := "Superelider Annotation"
  )
}


