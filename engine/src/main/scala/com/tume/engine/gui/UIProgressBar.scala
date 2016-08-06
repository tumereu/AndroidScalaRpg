package com.tume.engine.gui

import android.graphics._
import android.util.Log
import com.tume.engine.util.{D, Calc}

/**
  * Created by tume on 5/13/16.
  */
class UIProgressBar extends UIComponent {

  private var numerator, denominator = 0
  private var rawProgress : Option[Float] = None
  private var paintProgress = 0f
  private var overFlows = 0
  private var paint: Paint = null

  private var ticks = Vector.empty[ProgressBarTick]

  override def render(canvas: Canvas): Unit = {
    canvas.drawRoundRect(0, 0, width, height, cornerRadius, cornerRadius, UITheme.background)
    for (t <- ticks) {
      canvas.drawRoundRect(0, 0, width * t.percent, height, cornerRadius, cornerRadius, secondaryPaint((t.fade * 255).toInt))
    }
    canvas.drawRoundRect(0, 0, width * paintProgress, height, cornerRadius, cornerRadius, paint)
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
    this.rawProgress = raw
  }

  def overflow(n: Int): Unit = {
    this.overFlows += n
  }

  def progress = if (rawProgress.isDefined) rawProgress.get else if (denominator != 0) numerator.toFloat / denominator else 1f

  override def update(delta: Float): Unit = {
    super.update(delta)
    for (t <- this.ticks) t.fade -= delta.toFloat
    this.ticks = this.ticks.filterNot(_.fade <= 0)
    val diff = (overFlows + this.progress - this.paintProgress) * Calc.clamp(delta * 5, 0f, 1f)
    if (diff != 0) {
      val maxSpeed = delta * 20f
      this.paintProgress += Calc.clamp(diff, -maxSpeed, maxSpeed)
      if (paintProgress <= 0 && overFlows < 0) {
        paintProgress += 1
        overFlows -= 1
      }
      if (paintProgress >= 1 && overFlows > 0) {
        paintProgress -= 1
        overFlows -= 1
      }
    }
  }

}
class ProgressBarTick(val percent: Float) {
  var fade = 1f
}