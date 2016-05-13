package com.tume.engine.gui

import android.graphics.Canvas
import com.tume.engine.gui.event.UIEventListener

/**
  * Created by tume on 5/12/16.
  */
class UISystem {

  var components = Map[String, Vector[UIComponent]]()
  var activeComponents = Vector[UIComponent]()

  def init(views: Seq[UIView], listener: UIEventListener): Unit = {
    for (v <- views) {
      val vec = Vector.newBuilder[UIComponent]
      for (builder <- v.build) {
        vec += builder.resolve
      }
      val vecRes = vec.result()
      vecRes.foreach(_.view = Some(v.name))
      components += v.name -> vecRes
    }
    for (cmp <- components.values.flatten) {
      cmp.listener = Some(listener)
      cmp.toggleVisibility(false)
      cmp.init()
    }
    if (!views.isEmpty) {
      this.show(views.head.name)
    }
  }

  def update(delta: Double): Unit = {
    for (cmp <- components.values.flatten) {
      cmp.update(delta)
    }
  }

  def render(canvas: Canvas): Unit = {
    for (cmp <- activeComponents) {
      cmp.render(canvas)
    }
  }

  def show(views: String*): Unit = {
    for (cmp <- this.activeComponents) {
      cmp.toggleVisibility(false)
    }
    this.activeComponents = Vector.empty
    for (s <- views) {
      this.activeComponents = this.activeComponents ++ this.components(s)
    }
    for (cmp <- this.activeComponents) {
      cmp.toggleVisibility(true)
    }
  }

}
