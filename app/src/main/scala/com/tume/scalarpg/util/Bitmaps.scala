package com.tume.scalarpg.util

import android.content.Context
import android.graphics.{BitmapFactory, Bitmap}
import com.tume.scalarpg.R.drawable._

/**
  * Created by tume on 5/11/16.
  */
object Bitmaps {

  var context: Option[Context] = None

  private var decoded = Map[Int, Bitmap]()

  val floorsMarble = Array[Int](marble_floor1, marble_floor2, marble_floor3, marble_floor4, marble_floor5, marble_floor6)
  val floorsSandStone = Array[Int](floor_sand_stone0,floor_sand_stone1,floor_sand_stone2,
    floor_sand_stone3,floor_sand_stone4,floor_sand_stone5,floor_sand_stone6,floor_sand_stone7)

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
