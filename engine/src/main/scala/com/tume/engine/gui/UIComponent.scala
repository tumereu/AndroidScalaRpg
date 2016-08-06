package com.tume.engine.gui

import android.graphics.{Bitmap, Canvas, Paint}
import com.tume.engine.Input
import com.tume.engine.gui.event.{UIEvent, UIEventListener}
import com.tume.engine.model.{Shape, Rect}
import com.tume.engine.util.{Bitmaps, DisplayUtils}

/**
  * Created by tume on 5/12/16.
  */
abstract class UIComponent {
  import com.tume.engine.gui.UIState._
  protected var innerState = Normal

  var drawable = -1
  var id : Option[String] = None
  var view: Option[String] = None
  var listener: Option[UIEventListener] = None
  var uiSystem: UISystem = null

  var color1, color2, color3, color4: Int = -1

  var enabled = true
  var visible = true

  protected var bitmap: Option[Bitmap] = None

  var x, y, width, height = 0

  def render(canvas: Canvas) : Unit
  def init(): Unit = {
    bitmap = drawable match {
      case -1 => None
      case x => Some(Bitmaps.get(x))
    }
  }

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

  def onViewShow(): Unit = {
    toggleVisibility(true)
  }

  def toggleEnabled(boolean: Boolean): Unit = {
    this.enabled = boolean
  }

  def interactable = visible && enabled && uiSystem.isReceivingInput(this)

  def update(delta: Float): Unit = {
    if (Input.tap(boundingBox)) {
      onClick()
    }
    if (Input.touching(boundingBox)) {
      this.innerState = Pressed
      UIFocus.currentFocus = Some(this)
      if (Input.touchStarted(boundingBox)) {
        onTouch()
      }
    } else {
      this.innerState = Normal
    }
  }

  def onTouch(): Unit = {}
  def onClick(): Unit = {}

  def throwEvent(event: UIEvent): Unit = {
    if (this.listener.isDefined && this.view.isDefined && this.id.isDefined) {
      event.view = this.view
      event.id = this.id
      this.listener.get.onUIEvent(event)
    }
  }

  def boundingBox : Shape = Rect(x, y, x + width, y + height)

  def layer = 0

  def contains(uIComponent: UIComponent) : Boolean = this == uIComponent

  def onAddAsPopup(activePopups: Vector[UIComponent]): Unit = {}
  def onRemoveAsPopup(activePopups: Vector[UIComponent]): Unit = {}

  def find(id: String): Option[UIComponent] = {
    if (this.id.contains(id)) {
      Some(this)
    } else {
      None
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
  val cornerRadius = DisplayUtils.scale * 10

  val bitmapPaint = new Paint()

  val borderPaint = create(0xff999999, Paint.Style.FILL, 2 * DisplayUtils.scale)
  val borderPaintDisabled = create(0xff454545, Paint.Style.FILL, 2 * DisplayUtils.scale)
  val borderPaintPanel = create(0xff339999, Paint.Style.FILL)

  val fillPaintNormal = create(0xffAA6600, Paint.Style.FILL)
  val fillPaintDisabled = create(0xff898989, Paint.Style.FILL)
  val fillPaintPressed = create(0xff996699, Paint.Style.FILL)
  val fillPaintPanel = create(0xff606060, Paint.Style.FILL)

  val textPaint = create(0xffaaceaa)

  val background = create(0xff666666, Paint.Style.FILL)

  private def create(color: Int, style: Paint.Style = Paint.Style.FILL_AND_STROKE, strokeWidth: Float = DisplayUtils.scale, antialias: Boolean = true) : Paint = {
    val paint = new Paint()
    paint.setColor(color)
    paint.setStyle(style)
    paint.setStrokeWidth(strokeWidth)
    paint.setAntiAlias(antialias)
    paint
  }
}
