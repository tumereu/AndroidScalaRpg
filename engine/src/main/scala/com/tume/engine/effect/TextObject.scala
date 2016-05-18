package com.tume.engine.effect

import android.graphics.{Rect, Canvas, Paint}
import android.util.Log
import com.tume.engine.util.{LinearCurve, QuadraticCurve}

/**
  * Created by tume on 5/15/16.
  */
abstract class TextObject(val paint: Paint, val text: String, start: (Float, Float)) extends RenderableEffect {

  var x = start._1
  var y = start._2

  def render(canvas: Canvas): Unit = {
    val bounds = new Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    canvas.drawText(text, x - bounds.width / 2, y + bounds.height, paint)
  }
}
class FadingTextObject(p: Paint, t: String, s: (Float, Float), val fadeDuration: Float, val fadeDelay: Float = 0f) extends TextObject(p, t, s) {
  var currentDelay = fadeDelay
  var remaining = fadeDuration

  override def update(delta: Float): Unit = {
    if (currentDelay > 0) {
      currentDelay -= delta
      if (currentDelay < 0) {
        remaining += currentDelay
        currentDelay = 0
      }
    } else {
      remaining -= delta
      if (remaining < 0) {
        remaining = 0
      }
      this.paint.setAlpha((255 * remaining / fadeDuration).toInt)
    }
  }

  override def isRemovable = remaining <= 0
}
class FloatingTextObject(pa: Paint, te: String, st: (Float, Float), fadeDur: Float, val fadeDel: Float = 0f,
                              val floatSpeed: Float) extends FadingTextObject(pa, te, st, fadeDur, fadeDel) {

  override def update(delta: Float): Unit = {
    super.update(delta)
    this.y += floatSpeed * delta
  }
}

class HomingTextObject(pa: Paint, te: String, val start: (Float, Float), val target: (Float, Float), val dispersion: Float,
                       val homingDuration: Float) extends TextObject(pa, te, start) {

  var remainingDuration = homingDuration
  val angle = Math.random() * Math.PI * 2
  val middle = (x + Math.cos(angle).toFloat * dispersion, y + Math.sin(angle).toFloat * dispersion)

  val curve = new QuadraticCurve(start, middle, target)

  override def update(delta: Float): Unit = {
    remainingDuration -= delta
    if (remainingDuration < 0) {
      remainingDuration = 0
    }
    val t = 1 - remainingDuration / homingDuration
    val coordinates = curve.eval(Math.pow(t.toFloat, 1.4).toFloat)
    x = coordinates._1
    y = coordinates._2
  }

  override def isRemovable: Boolean = remainingDuration <= 0
}
