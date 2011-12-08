package net.tixxit.superelider

import scala.tools.nsc.Global
import scala.tools.nsc.plugins.Plugin


/**
 * A plugin that provides similar features to @elidable.
 */
class SuperEliderPlugin(val global: Global) extends Plugin {
  val name = "superelider"
  val description = "A more general version of @elidable."

  val runsAfter = List[String]("liftcode")

  var elisionLevels: Map[String,Int] = Map("" -> Int.MinValue)
  
  override val optionsHelp = Some(
    "  -P:" + name + ":elide-below:label:level    remove calls to methods annotated with label and below level.")

  override def processOptions(options: List[String], error: String => Unit) {
    import SuperEliderPlugin.{ LabelledOpt, UnlabelledOpt }

    for (opt <- options) opt match {
      case LabelledOpt(label, level) => elisionLevels += (label -> level.toInt)
      case UnlabelledOpt(level) => elisionLevels += ("" -> level.toInt)
      case opt =>
        error("Unknown option: -P:" + name + ":" + opt)
    }
  }

  val components = List(new SuperElider(global, this))
}

object SuperEliderPlugin {
  val LabelledOpt = """elide-below:([a-zA-Z_][\w\.]*(?::[a-zA-Z_][\w\.]*)*):(-?\d+)""" r
  val UnlabelledOpt = """elide-below:(-?\d+)""" r
}
