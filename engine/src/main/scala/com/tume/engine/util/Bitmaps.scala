package com.tume.engine.util

import android.content.Context
import android.graphics.{BitmapFactory, Bitmap}

/**
  * Created by tume on 5/11/16.
  */
object Bitmaps {

  var context: Option[Context] = None

  private var decoded = Map[Int, Bitmap]()

  def get(resource: Int): Bitmap = {
    if (context.isEmpty) {
      throw new RuntimeException("Context needs to be set before trying to decode resources")
    }
    if (!decoded.contains(resource)) {
      val bitmap = BitmapFactory.decodeResource(context.get.getResources, resource)
      decoded += resource -> bitmap
    }
    decoded(resource)
  }

  def random(array: Array[Int]): Int = array((Math.random() * array.length).toInt)
}
