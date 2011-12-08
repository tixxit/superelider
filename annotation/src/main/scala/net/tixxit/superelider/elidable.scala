package net.tixxit.superelider

import scala.annotation.{ elidable => _, _ }

final class elidable(final val label: String, final val level: Int) extends annotation.StaticAnnotation

object elidable {
  final val ALL = Int.MinValue
  final val FINE = 500
  final val OFF = Int.MaxValue
}

