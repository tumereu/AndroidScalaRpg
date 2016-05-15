package com.tume.engine

import android.graphics.Canvas
import com.tume.engine.effect.EffectSystem
import com.tume.engine.gui.{UISystem, UIView}
import com.tume.engine.gui.event.{UIEvent, UIEventListener}

/**
  * Created by tume on 5/12/16.
  */
trait Game extends UIEventListener {

  var uiSystem : UISystem = null
  var effectSystem: EffectSystem = null

  def init()

  def update(delta: Double)
  def render(canvas: Canvas)

  def views: Seq[UIView]

  def onUIEvent(event: UIEvent)

  def findUIComponent(id: String) = uiSystem.findComponent(id)

}
