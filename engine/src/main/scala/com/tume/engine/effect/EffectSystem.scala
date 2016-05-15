package com.tume.engine.effect

import android.graphics.Canvas

/**
  * Created by tume on 5/15/16.
  */
class EffectSystem {

  var effects = Vector.empty[RenderableEffect]

  def update(delta: Double): Unit = {
    for (e <- effects) e.update(delta.toFloat)
    effects = effects.filterNot(_.isRemovable)
  }

  def render(canvas: Canvas): Unit = {
    for (e <- effects) e.render(canvas)
  }

  def add(renderableEffect: RenderableEffect): Unit = {
    effects = effects :+ renderableEffect
  }

}