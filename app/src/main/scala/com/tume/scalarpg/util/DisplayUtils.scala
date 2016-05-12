package com.tume.scalarpg.util

import android.content.Context

/**
  * Created by tume on 5/11/16.
  */
object DisplayUtils {

  var scale = 0F

  def init(context: Context): Unit = {
    scale = context.getResources.getDisplayMetrics.scaledDensity
  }

}
