package com.tume.engine.effect

import android.graphics.Canvas
import android.util.Log

/**
  * Created by tume on 5/15/16.
  */
class EffectSystem {

  var effectsBelow = Vector.empty[RenderableEffect]
  var effectsAbove = Vector.empty[RenderableEffect]

  def update(delta: Float): Unit = {
    for (e <- effectsBelow) e.update(delta)
    for (e <- effectsAbove) e.update(delta)
    for (e <- effectsBelow.filter(_.isRemovable)) e.onRemove()
    for (e <- effectsAbove.filter(_.isRemovable)) e.onRemove()
    effectsBelow = effectsBelow.filterNot(_.isRemovable)
    effectsAbove = effectsAbove.filterNot(_.isRemovable)
  }

  def renderBelow(canvas: Canvas): Unit = {
    for (e <- effectsBelow) e.render(canvas)
  }

  def renderAbove(canvas: Canvas): Unit = {
    for (e <- effectsAbove) e.render(canvas)
  }

  def add(renderableEffect: RenderableEffect): Unit = {
    renderableEffect.layer match {
      case EffectLayer.AboveAll => effectsAbove = effectsAbove :+ renderableEffect
      case EffectLayer.BelowAll => effectsBelow = effectsBelow :+ renderableEffect
    }
  }

}