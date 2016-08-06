package com.tume.engine.util

import android.util.Log

/**
  * Created by tume on 5/15/16.
  */
abstract class Curve {
  def eval(t: Float): (Float, Float)
}
case class LinearCurve(p0: (Float, Float), p1: (Float, Float)) extends Curve {
  override def eval(t: Float): (Float, Float) = (p0._1 + (p1._1 - p0._1) * t, p0._2 + (p1._2 - p0._2) * t)
}
case class QuadraticCurve(val p0: (Float, Float), val p1: (Float, Float), val p2: (Float, Float)) extends Curve {
  override def eval(t: Float): (Float, Float) = {
    val x = (1 - t) * (1 - t) * p0._1 + 2 * (1 - t) * t * p1._1 + t * t * p2._1
    val y = (1 - t) * (1 - t) * p0._2 + 2 * (1 - t) * t * p1._2 + t * t * p2._2
    (x.toFloat, y.toFloat)
  }
}