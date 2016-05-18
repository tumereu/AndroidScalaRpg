package com.tume.engine.gui

import android.graphics._
import android.util.Log

/**
  * Created by tume on 5/13/16.
  */
class UIProgressBar extends UIComponent {

  private var numerator, denominator = 0
  private var rawProgress : Option[Float] = None
  private var paint: Paint = null

  private var ticks = Vector.empty[ProgressBarTick]

  override def render(canvas: Canvas): Unit = {
    canvas.drawRoundRect(0, 0, width, height, cornerRadius, cornerRadius, UITheme.background)
    for (t <- ticks) {
      canvas.drawRoundRect(0, 0, width * t.percent, height, cornerRadius, cornerRadius, secondaryPaint((t.fade * 255).toInt))
    }
    canvas.drawRoundRect(0, 0, width * progress, height, cornerRadius, cornerRadius, paint)
    if (UIFocus.currentFocus.contains(this)) {
      val text = numerator + "/" + denominator
      val pa = new Paint()
      pa.setTextSize(this.height / 2)
      pa.setColor(0xff000000)
      val bounds = new Rect()
      pa.getTextBounds(text, 0, text.length, bounds)
      canvas.drawText(text, width / 2 - bounds.width() / 2, height / 2 + bounds.height() / 2, pa)
    }
  }

  private def cornerRadius = height.toFloat / 6

  override def init(): Unit = {
    paint = new Paint()
    paint.setStyle(Paint.Style.FILL)
    paint.setShader(new LinearGradient(0, 0, 0, height / 2, color1, color2, Shader.TileMode.MIRROR))
  }

  def secondaryPaint(alpha: Int): Paint = {
    val c1 = color3 & 0x00ffffff | (alpha << 24)
    val c2 = color4 & 0x00ffffff | (alpha << 24)
    val pa = new Paint()
    pa.setStyle(Paint.Style.FILL)
    pa.setShader(new LinearGradient(0, 0, 0, height / 2, c1, c2, Shader.TileMode.MIRROR))
    pa
  }

  def tick(percent: Float): Unit = {
    ticks = ticks :+ new ProgressBarTick(percent)
  }

  def tick(): Unit = tick(this.progress)

  def updateProgress(numerator: Int, denominator: Int): Unit = {
    this.numerator = numerator
    this.denominator = denominator
  }

  def updateRawProgress(raw: Option[Float]): Unit = {
    this.rawProgress = raw;
  }

  def progress = if (rawProgress.isDefined) rawProgress.get else numerator.toFloat / denominator

  override def update(delta: Double): Unit = {
    super.update(delta)
    for (t <- this.ticks) t.fade -= delta.toFloat
    this.ticks = this.ticks.filterNot(_.fade <= 0)
  }

}
class ProgressBarTick(val percent: Float) {
  var fade = 1f
}