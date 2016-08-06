package com.tume.engine.model

import com.tume.engine.util.Calc._

/**
  * Created by tume on 7/7/16.
  */
trait Shape {
  def width: Float
  def height: Float
  def contains(point: (Float, Float)) : Boolean
  def contains(v: Vec2) : Boolean  = contains((v.x, v.y))
  def intersects(shape: Shape): Boolean
  def center: (Float, Float)
  def shrink(t: Float): Shape
  def enlarge(t: Float): Shape
}
case class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float) extends Shape {
  if (left > right || top > bottom) {
    throw new ImpossibleShapeException("Rect area cannot be negative, left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom)
  }
  override def width = right - left
  override def height = bottom - top

  override def contains(point: (Float, Float)): Boolean = {
    point._1 >= left && point._1 <= right && point._2 >= top && point._2 <= bottom
  }
  override def intersects(shape: Shape): Boolean = shape match {
    case Rect(l, t, r, b) => contains((l, t)) || contains((t, r)) || contains((r, b)) || contains(b, l)
    case c: Circle => c.contains((clamp(c.x, left, right), clamp(c.y, top, bottom)))
    case p: Point => p.intersects(this)
    case _ => ???
  }
  override def center = (left + width / 2, top + height / 2)
  override def shrink(t: Float): Rect = new Rect(left + t, top + t, right - t, bottom - t)
  override def enlarge(t: Float): Rect = new Rect(left - t, top - t, right + t, bottom + t)
}
case class Circle(val x: Float, val y: Float, val radius: Float) extends Shape {
  if (radius < 0) {
    throw new ImpossibleShapeException("Circle radius cannot be negative, radius: " + radius)
  }
  override def width = radius * 2
  override def height = radius * 2
  override def contains(point: (Float, Float)): Boolean = {
    Math.sqrt((point._1-x)*(point._1-x)+(point._2-y)*(point._2-y)) <= radius
  }
  override def intersects(shape: Shape): Boolean = shape match {
    case rect: Rect => rect.intersects(this)
    case Circle(rx, ry, r) => Math.sqrt((rx-x)*(rx-x)+(ry-y)*(ry-y)) <= radius + r
    case p: Point => p.intersects(this)
    case _ => ???
  }
  override def center = (x, y)
  override def shrink(t: Float) : Circle = Circle(x, y, radius - t)
  override def enlarge(t: Float) : Circle = Circle(x, y, radius + t)
}
case class Point(val x: Float, val y: Float) extends Shape {
  override def width: Float = 1f
  override def height: Float = 1f
  override def center: (Float, Float) = (x, y)
  override def contains(point: (Float, Float)): Boolean = point._1 == x && point._2 == y
  override def intersects(shape: Shape): Boolean = shape.contains((x, y))
  override def shrink(t: Float) = throw new ImpossibleShapeException("A point cannot be shrunk")
  override def enlarge(t: Float) = throw new ImpossibleShapeException("A point cannot be enlarged")
}
class ImpossibleShapeException(msg: String) extends RuntimeException(msg) {

}
