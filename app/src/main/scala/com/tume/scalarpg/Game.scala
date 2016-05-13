package com.tume.scalarpg

import android.graphics.Canvas
import com.tume.scalarpg.gui.UIView
import com.tume.scalarpg.gui.event.{UIEventListener, UIEvent}

/**
  * Created by tume on 5/12/16.
  */
trait Game extends UIEventListener {

  def update(delta: Double)
  def render(canvas: Canvas)

  def views: Seq[UIView]

  def onUIEvent(event: UIEvent)

}
