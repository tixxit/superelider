package net.tixxit.superelider

import scala.annotation.{ elidable => _, _ }

final class elidable(final val level: Int, final val label: String = "") extends annotation.StaticAnnotation

object elidable {
  final val ALL = Int.MinValue
  final val FINE = 500
  final val OFF = Int.MaxValue
}

