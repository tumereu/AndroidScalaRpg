package com.tume.engine.model

import com.tume.engine.util.Calc

/**
  * Created by tume on 7/24/16.
  */
class Vec2(val x: Float, val y: Float) {

  def normalize = {
    if (x == 0 && y == 0) {
      Vec2.zero
    } else {
      val tmp = Calc.normalise((x, y))
      Vec2(tmp._1, tmp._2)
    }
  }

  def normals : (Vec2, Vec2) = (Vec2(-y, x), Vec2(y, -x))
  def normal1 : Vec2 = normals._1
  def normal2 : Vec2 = normals._2

  def invert = Vec2(-x, -y)
  def invertX = Vec2(-x, y)
  def invertY = Vec2(x, y)
  def len = Calc.dist((0f, 0f), (x, y))
  def dist(vec: Vec2): Float = Calc.dist((vec.x, vec.y), (x, y))
  def dist(vec: Option[Vec2]): Float = if (vec.isEmpty) 0f else Calc.dist((vec.get.x, vec.get.y), (x, y))

  def +(vec: Vec2) = Vec2(x + vec.x, y + vec.y)
  def -(vec: Vec2) = Vec2(x - vec.x, y - vec.y)
  def *(t: Float) = Vec2(x * t, y * t)
  def /(t: Float) = Vec2(x / t, y / t)

  def angle = Calc.angle((x, y))

  def clamp(left: Float, top: Float, right: Float, bottom: Float) : Vec2 = {
    Vec2(Calc.clamp(x, left, right), Calc.clamp(y, top, bottom))
  }

  def clamp(rect: Rect) : Vec2 = clamp(rect.left, rect.top, rect.right, rect.bottom)

  def lerp(to: Vec2, t: Float) = this + (to - this) * t

}
object Vec2 {
  def apply(x: Float, y: Float) = new Vec2(x, y)
  def zero = Vec2(0, 0)
}
object Vec2R {
  def insideUnitCircle : Vec2 = {
    val dir = Calc.dir(Calc.randAngle)
    Vec2(dir._1, dir._2)
  }
}
