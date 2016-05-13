package com.tume.engine.gui

import android.graphics.{Bitmap, Canvas}
import com.tume.engine.gui.event.ButtonEvent
import com.tume.engine.util.{DisplayUtils, Bitmaps}

/**
  * Created by tume on 5/12/16.
  */
class UIButton extends UIComponent {

  import com.tume.engine.gui.UIState._

  override def render(canvas: Canvas): Unit = {
    val fillPaint = this.state match {
      case Pressed => UITheme.fillPaintPressed
      case _ => UITheme.fillPaintNormal
    }
    val b = DisplayUtils.scale * 2
    canvas.drawRoundRect(0, 0, width, height, DisplayUtils.scale * 10, DisplayUtils.scale * 10, UITheme.borderPaint)
    canvas.drawRoundRect(b, b, width - b, height - b, DisplayUtils.scale * 10, DisplayUtils.scale * 10, fillPaint)
    if (bitmap.isDefined) {
      val b = bitmap.get
      canvas.drawBitmap(b, width / 2 - b.getScaledWidth(canvas) / 2, height / 2 - b.getScaledHeight(canvas) / 2, UITheme.bitmapPaint)
    }
  }

  override def onTouch(): Unit = throwEvent(ButtonEvent())
}
