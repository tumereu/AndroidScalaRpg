package com.tume.engine.gui

import android.graphics.{Bitmap, Canvas}
import com.tume.engine.gui.event.ButtonEvent
import com.tume.engine.util.{DisplayUtils, Bitmaps}

/**
  * Created by tume on 5/12/16.
  */
class UIButton extends UIComponent {

  import com.tume.engine.gui.UIState._

  private var bitmap: Option[Bitmap] = None

  def init(): Unit = {
    bitmap = drawable match {
      case -1 => None
      case x => Some(Bitmaps.get(x))
    }
  }

  override def render(canvas: Canvas): Unit = {
    val fillPaint = this.state match {
      case Pressed => UITheme.fillPaintPressed
      case _ => UITheme.fillPaintNormal
    }
    canvas.drawRoundRect(x, y, x + width, y + width, DisplayUtils.scale * 10, DisplayUtils.scale * 10, fillPaint)
    canvas.drawRoundRect(x, y, x + width, y + width, DisplayUtils.scale * 10, DisplayUtils.scale * 10, UITheme.borderPaint)
    if (bitmap.isDefined) {
      val b = bitmap.get
      canvas.drawBitmap(b, x + width / 2 - b.getScaledWidth(canvas) / 2, y + height / 2 - b.getScaledHeight(canvas) / 2, UITheme.bitmapPaint)
    }
  }

  override def onTouch(): Unit = throwEvent(ButtonEvent())
}