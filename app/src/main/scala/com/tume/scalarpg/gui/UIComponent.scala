package com.tume.scalarpg.gui

import android.graphics.{Paint, Canvas}
import com.tume.scalarpg.gui.event.{UIEvent, UIEventListener}
import com.tume.scalarpg.util.{DisplayUtils, Input}

/**
  * Created by tume on 5/12/16.
  */
abstract class UIComponent {

  import com.tume.scalarpg.gui.UIState._
  protected var innerState = Normal

  var drawable = -1
  var id : Option[String] = None
  var view: Option[String] = None
  var listener: Option[UIEventListener] = None

  protected var enabled = true
  protected var visible = true

  var x, y, width, height = 0

  def render(canvas: Canvas) : Unit
  def init()

  def state : UIState = {
    if (!visible) {
      Hidden
    } else if (!enabled) {
      Disabled
    } else {
      if (this.innerState == Normal) {
        if (UIFocus.currentFocus.contains(this)) {
          Focused
        } else {
          Normal
        }
      } else {
        innerState
      }
    }
  }


  def toggleVisibility(boolean: Boolean): Unit = {
    this.visible = boolean
  }

  def toggleEnabled(boolean: Boolean): Unit = {
    this.enabled = boolean
  }

  def update(delta: Double): Unit = {
    if (Input.isTouchInside(x, y, width, height)) {
      this.innerState = Pressed
      UIFocus.currentFocus = Some(this)
      if (Input.touchStartedThisFrame) {
        onTouch()
      }
    } else {
      this.innerState = Normal
    }
  }

  def onTouch(): Unit = {

  }

  def throwEvent(event: UIEvent): Unit = {
    if (this.listener.isDefined && this.view.isDefined && this.id.isDefined) {
      event.view = this.view
      event.id = this.id
      this.listener.get.onUIEvent(event)
    }
  }

}
object UIState extends Enumeration {
  type UIState = Value
  val Normal, Disabled, Hidden, Focused, Pressed = Value
}
object UIFocus {
  var currentFocus: Option[UIComponent] = None
}
object UITheme {
  val bitmapPaint = new Paint()

  val borderPaint = create(0xff999999, Paint.Style.STROKE, 2 * DisplayUtils.scale)

  val fillPaintNormal = create(0xffAA6600, Paint.Style.FILL)
  val fillPaintPressed = create(0xff996699, Paint.Style.FILL)

  private def create(color: Int, style: Paint.Style = Paint.Style.FILL_AND_STROKE, strokeWidth: Float = DisplayUtils.scale, antialias: Boolean = true) : Paint = {
    val paint = new Paint()
    paint.setColor(color)
    paint.setStyle(style)
    paint.setStrokeWidth(strokeWidth)
    paint.setAntiAlias(antialias)
    paint
  }
}
