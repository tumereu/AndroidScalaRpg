package com.tume.scalarpg.model

import android.graphics.{Paint, Canvas}
import com.tume.engine.util.{DisplayUtils, Bitmaps}

/**
  * Created by tume on 5/11/16.
  */
class Tile(val x: Int, val y: Int, floorResource: Int) {

  val bitmap = Bitmaps.get(floorResource)

  var objects: Vector[TileObject] = Vector.empty

  def render(canvas: Canvas): Unit = {
    canvas.drawBitmap(bitmap, 0, 0, new Paint())
  }

  def location = (x, y)

  def renderObjects(canvas: Canvas): Unit = {
    for (o <- this.objects) {
      o.render(canvas)
    }
  }

  def addObject(tileObject: TileObject): Unit = {
    objects = objects :+ tileObject
    tileObject.currentTile = Some(this)
  }

  def removeObject(tileObject: TileObject): Unit = {
    objects = objects.filterNot(_ == tileObject)
    if (tileObject.currentTile.contains(this)) {
      tileObject.currentTile = None
    }
  }

  def canBeEntered = this.objects.find(!_.isPassable).isEmpty

}
object Tile {
  val size = 32 * DisplayUtils.scale
}
