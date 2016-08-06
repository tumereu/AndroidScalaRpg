package com.tume.engine.effect

import android.graphics.Canvas

/**
  * Created by tume on 5/15/16.
  */
trait RenderableEffect {

  def layer = EffectLayer.AboveAll

  var onRemove = () => {}

  def update(delta: Float)
  def isRemovable: Boolean
  def render(canvas: Canvas)

}
object EffectLayer extends Enumeration {
  type EffectLayer = Value
  val BelowAll, AboveAll = Value
}
