package com.tume.engine

import android.graphics.Canvas
import com.tume.engine.gui.UIView
import com.tume.engine.gui.event.{UIEvent, UIEventListener}

/**
  * Created by tume on 5/12/16.
  */
trait Game extends UIEventListener {

  def init()

  def update(delta: Double)
  def render(canvas: Canvas)

  def views: Seq[UIView]

  def onUIEvent(event: UIEvent)

}
