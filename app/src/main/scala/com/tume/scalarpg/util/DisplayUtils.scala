package com.tume.scalarpg.util

import android.content.Context

/**
  * Created by tume on 5/11/16.
  */
object DisplayUtils {

  var scale = 0F
  var screenWidth, screenHeight, screenSize = 0

  def init(context: Context): Unit = {
    scale = context.getResources.getDisplayMetrics.scaledDensity
    screenWidth = context.getResources.getDisplayMetrics.widthPixels
    screenHeight = context.getResources.getDisplayMetrics.heightPixels
    screenSize = Math.min(screenWidth, screenHeight)
  }

}
