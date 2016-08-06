package com.tume.engine.model

import android.graphics.{Paint, Canvas}

/**
  * Created by tume on 7/7/16.
  */
trait Background {
  def update(delta: Float) : Unit = {}
  def render(canvas: Canvas)
}
case class ColorBackground(val color: Int) extends Background {
  val paint = new Paint()
  paint.setColor(color)
  override def render(canvas: Canvas): Unit = {
    canvas.drawPaint(paint)
  }
}
