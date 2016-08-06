package com.tume.engine.util

import android.content.Context

/**
  * Created by tume on 5/11/16.
  */
object DisplayUtils {

  var scale = 0F
  var screenWidth, screenHeight, screenSize = 0

  def init(context: Context): Unit = {
    scale = context.getResources.getDisplayMetrics.scaledDensity
    resize(context.getResources.getDisplayMetrics.widthPixels, context.getResources.getDisplayMetrics.heightPixels)
  }

  def resize(width: Int, height: Int): Unit = {
    screenWidth = width
    screenHeight = height
    screenSize = Math.min(screenWidth, screenHeight)
  }

}
