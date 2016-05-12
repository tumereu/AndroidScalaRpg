package com.tume.scalarpg.gui

import com.tume.scalarpg.util.Input

/**
  * Created by tume on 5/12/16.
  */
abstract class UIComponent {

  import com.tume.scalarpg.gui.UISTate._
  private var innerState = Normal

  private var enabled = true
  private var visible = true

  private var x, y = 0

  def width: Int
  def height: Int

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
    } else {
      this.innerState = Normal
    }
  }

}
object UISTate extends Enumeration {
  type UIState = Value
  val Normal, Disabled, Hidden, Focused, Pressed = Value
}
object UIFocus {
  var currentFocus: Option[UIComponent] = None
}
