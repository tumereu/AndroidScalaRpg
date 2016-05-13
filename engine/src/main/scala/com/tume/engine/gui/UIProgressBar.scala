package com.tume.engine.gui

import android.graphics.{Shader, LinearGradient, Paint, Canvas}

/**
  * Created by tume on 5/13/16.
  */
class UIProgressBar extends UIComponent {

  private var progress = 0f
  private var paint: Paint = null

  override def render(canvas: Canvas): Unit = {
    canvas.drawRect(0, 0, width, height, UITheme.background)
    canvas.drawRect(0, 0, width * progress, height, paint)
  }

  override def init(): Unit = {
    paint = new Paint()
    paint.setStyle(Paint.Style.FILL)
    paint.setShader(new LinearGradient(0, 0, 0, height, color1, color2, Shader.TileMode.MIRROR))
  }

  def update(progress: Float) = {
    this.progress = progress
  }
}
