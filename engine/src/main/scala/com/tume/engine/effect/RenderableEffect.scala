package com.tume.engine.effect

import android.graphics.Canvas

/**
  * Created by tume on 5/15/16.
  */
trait RenderableEffect {

  var onRemove = () => {}

  def update(delta: Float)
  def isRemovable: Boolean
  def render(canvas: Canvas)

}
