package com.tume.scalarpg.model

import android.graphics.{Paint, Bitmap, Canvas}

/**
  * Created by tume on 5/11/16.
  */
class TileObject {

  var currentTile: Option[Tile] = None
  var bitmap: Option[Bitmap] = None
  var relativeSize = TileObject.relativeSize

  def isPassable = true

  def render(canvas: Canvas): Unit = {
    if (this.bitmap.isDefined) {
      val translation = Tile.size / 2
      canvas.translate(translation, translation)
      canvas.scale(relativeSize, relativeSize)
      canvas.translate(-translation, -translation)

      canvas.drawBitmap(bitmap.get, 0, 0, new Paint())

      canvas.translate(translation, translation)
      canvas.scale(1F / relativeSize, 1F / relativeSize)
      canvas.translate(-translation, -translation)
    }
  }

}
object TileObject {
  val relativeSize = 0.75f
}
