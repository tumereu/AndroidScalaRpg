package com.tume.engine.util

import android.util.Log

/**
  * Created by tume on 8/5/16.
  */
object D {
  def apply(s: Any): Unit = {
    Log.d("EngineDebug", "" + s)
  }
}
