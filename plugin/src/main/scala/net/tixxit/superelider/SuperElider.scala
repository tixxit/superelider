package net.tixxit.superelider

import scala.tools.nsc._
import scala.tools.nsc.plugins.PluginComponent
import scala.tools.nsc.transform.Transform


/** 
 * This provides a simple transformer that will replace method calls to
 * methods with the `@elidable` annotation that are subject to elision with
 * the `Unit` constant.
 */
class SuperElider(val global: Global, plugin: SuperEliderPlugin)
                                                    extends PluginComponent
                                                       with Transform {
  import global._
  import definitions._

  val runsAfter = List[String]("liftcode")

  val phaseName = "superelider"

  def newTransformer(unit: CompilationUnit) = new SuperEliderTransformer

  lazy val ElidableClass = definitions.getClass("net.tixxit.superelider.elidable")

  def elisionLevel(label: String): Option[Int] = plugin.elisionLevels get label


  class SuperEliderTransformer extends /*Typing*/ Transformer {

    def replaceWithUnit(tree: Tree): Tree =
      Literal(Constant()) setPos tree.pos setType UnitClass.tpe

    def isElidable(tree: Tree): Boolean = {

      // Get the symbol of the method from an Apply (and friends).
      val sym = treeInfo.methPart(tree).symbol

      if (sym != null) {
        val elidable = for {
          ann   <- (sym getAnnotation ElidableClass)
          label <- ann stringArg 0
          level <- ann intArg 1
          min   <- (label.split(":").scanLeft(List[String]())(_ :+ _).reverse
                      map (_ mkString ":") flatMap (elisionLevel(_)) headOption)
        } yield (level < min)

        elidable getOrElse false

      } else {
        false
      }
    }

    def preTransform(tree: Tree): Tree = tree match {
      case _ if (isElidable(tree)) => replaceWithUnit(tree)
      case _ => tree
    }

    def postTransform(tree: Tree): Tree = tree

    override def transform(tree: Tree): Tree = {
      postTransform(super.transform(preTransform(tree)))
    }
  }
}
