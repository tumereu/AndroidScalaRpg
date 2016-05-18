package com.tume.engine.gui

import android.graphics.Canvas
import android.util.Log
import com.tume.engine.gui.event.{UIEvent, UIEventListener}
import com.tume.engine.util.DisplayUtils

/**
  * Created by tume on 5/18/16.
  */
class UIPopupPanel extends UIComponent {

  var border = (DisplayUtils.scale * 2).toInt
  var margin = border + (DisplayUtils.scale * 2).toInt

  var currentLayer = 0

  var components = Vector.empty[UIComponent]

  override def render(canvas: Canvas): Unit = {
    canvas.drawRoundRect(0, 0, width, height, UITheme.cornerRadius, UITheme.cornerRadius, UITheme.borderPaintPanel)
    canvas.drawRoundRect(border, border, width - border, height - border, UITheme.cornerRadius, UITheme.cornerRadius, UITheme.fillPaintPanel)
    canvas.save()
    canvas.translate(-x, -y)
    for (c <- components) {
      canvas.save()
      canvas.translate(c.x, c.y)
      c.render(canvas)
      canvas.restore()
    }
    canvas.restore()
  }

  override def init(): Unit = {
    super.init()
    for (c <- components) {
      c.uiSystem = uiSystem
      c.view = view
      c.listener = listener
      c.init()
    }
    toggleVisibility(false)
  }

  override def toggleVisibility(boolean: Boolean): Unit = {
    super.toggleVisibility(boolean)
    components.foreach(_.toggleVisibility(boolean))
  }

  override def toggleEnabled(boolean: Boolean): Unit = {
    super.toggleEnabled(boolean)
    components.foreach(_.toggleEnabled(boolean))
  }

  override def update(delta: Double): Unit = {
    super.update(delta)
    components.foreach(_.update(delta))
  }


  def += (uIComponent: UIComponent): Unit = {
    components = components :+ uIComponent
    this.validateBounds()
  }

  def validateBounds(): Unit = {
    var right, bottom = -1
    x = this.components(0).x
    y = this.components(0).y
    for (c <- this.components) {
      if (this.x > c.x) {
        this.x = c.x
      }
      if (this.y > c.y) {
        this.y = c.y
      }
      if (right < c.x + c.width) {
        right = c.x + c.width
      }
      if (bottom < c.y + c.height) {
        bottom = c.y + c.height
      }
    }
    this.width = right - this.x + margin * 2
    this.height = bottom - this.y + margin * 2
    this.x -= margin
    this.y -= margin
  }

  override def contains(uIComponent: UIComponent) : Boolean = this == uIComponent || components.contains(uIComponent)

  override def onAddAsPopup(activePopups: Vector[UIComponent]): Unit = {
    currentLayer = activePopups.size
    toggleVisibility(true)
  }

  override def onRemoveAsPopup(activePopups: Vector[UIComponent]): Unit = {
    currentLayer = activePopups.size
    toggleVisibility(false)
  }

  override def onViewShow(): Unit = {
    super.onViewShow()
    toggleVisibility(false)
  }

  override def find(id: String): Option[UIComponent] = {
    val p = components.find(_.id.contains(id))
    if (p.isDefined) {
      p
    } else {
      super.find(id)
    }
  }

  override def layer = 100 + currentLayer
}
