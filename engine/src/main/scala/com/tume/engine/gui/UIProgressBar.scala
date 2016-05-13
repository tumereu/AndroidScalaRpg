package com.tume.engine.gui

import android.graphics.{Shader, LinearGradient, Paint, Canvas}
import android.util.Log

/**
  * Created by tume on 5/13/16.
  */
class UIProgressBar extends UIComponent {

  private var progress = 0f
  private var paint: Paint = null

  override def render(canvas: Canvas): Unit = {
    canvas.drawRoundRect(0, 0, width, height, cornerRadius, cornerRadius, UITheme.background)
    canvas.drawRoundRect(0, 0, width * progress, height, cornerRadius, cornerRadius, paint)
  }

  private def cornerRadius = height.toFloat / 6

  override def init(): Unit = {
    paint = new Paint()
    paint.setStyle(Paint.Style.FILL)
    paint.setShader(new LinearGradient(0, 0, 0, height / 2, color1, color2, Shader.TileMode.MIRROR))
  }

  def updateProgress(progress: Float): Unit = {
    this.progress = progress
  }

  def updateProgress(progress: Double): Unit = {
    this.updateProgress(progress.toFloat)
  }
}
