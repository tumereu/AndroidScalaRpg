package com.tume.engine.gui

import android.graphics.{Rect, Paint, Bitmap, Canvas}
import android.util.Log
import com.tume.engine.gui.event.ButtonEvent
import com.tume.engine.util.{DisplayUtils, Bitmaps}

/**
  * Created by tume on 5/12/16.
  */
class UIButton extends UIComponent {

  import com.tume.engine.gui.UIState._

  var cornerText = ""

  override def render(canvas: Canvas): Unit = {
    val fillPaint = this.state match {
      case Pressed => UITheme.fillPaintPressed
      case Disabled => UITheme.fillPaintDisabled
      case _ => UITheme.fillPaintNormal
    }
    val borderPaint = state match {
      case Disabled => UITheme.borderPaintDisabled
      case _ => UITheme.borderPaint
    }
    val b = DisplayUtils.scale * 2
    canvas.drawRoundRect(0, 0, width, height, UITheme.cornerRadius, UITheme.cornerRadius, borderPaint)
    canvas.drawRoundRect(b, b, width - b, height - b, UITheme.cornerRadius, UITheme.cornerRadius, fillPaint)
    if (bitmap.isDefined) {
      val b = bitmap.get
      canvas.drawBitmap(b, width / 2 - b.getScaledWidth(canvas) / 2, height / 2 - b.getScaledHeight(canvas) / 2, UITheme.bitmapPaint)
    }
    if (cornerText.length > 0) {
      val p = UITheme.textPaint
      p.setTextSize(DisplayUtils.scale * 15)
      val bounds = new Rect()
      p.getTextBounds(cornerText, 0, cornerText.length, bounds)
      canvas.drawText(cornerText, width - b * 3 - bounds.width(), b * 3 + bounds.height(), p)
    }
  }

  def onPress() : Unit = {
    throwEvent(ButtonEvent())
  }

  override def onClick(): Unit = onPress()
}
