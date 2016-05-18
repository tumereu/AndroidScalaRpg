package com.tume.engine.effect

import android.graphics.Canvas
import android.util.Log

/**
  * Created by tume on 5/15/16.
  */
class EffectSystem {

  var effects = Vector.empty[RenderableEffect]

  def update(delta: Double): Unit = {
    for (e <- effects) e.update(delta.toFloat)
    for (e <- effects.filter(_.isRemovable)) e.onRemove()
    effects = effects.filterNot(_.isRemovable)
  }

  def render(canvas: Canvas): Unit = {
    for (e <- effects) e.render(canvas)
  }

  def add(renderableEffect: RenderableEffect): Unit = {
    effects = effects :+ renderableEffect
  }

}