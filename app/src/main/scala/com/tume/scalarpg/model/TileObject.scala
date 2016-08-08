package com.tume.scalarpg.model

import android.graphics.{Paint, Bitmap, Canvas}
import com.tume.engine.model.Vec2
import com.tume.scalarpg.TheGame

/**
  * Created by tume on 5/11/16.
  */
class TileObject {

  var currentTile: Option[Tile] = None
  var bitmap: Option[Bitmap] = None

  def relativeSize = TileObject.relativeSize

  def isPassable = true

  def render(canvas: Canvas): Unit = {
    if (this.bitmap.isDefined) {
      val translation = Tile.size / 2
      canvas.save()
      canvas.translate(translation, translation)
      canvas.scale(relativeSize, relativeSize)
      canvas.translate(-translation, -translation)

      canvas.drawBitmap(bitmap.get, offSet.x, offSet.y, bitmapPaint)

      canvas.restore()
    }
  }

  def offSet = Vec2.zero

  def bitmapPaint = new Paint()

  def roundEnded(game: TheGame): Unit = { }

  def turnEnded(game: TheGame, timeDelta: Float): Unit = { }

}
object TileObject {
  val relativeSize = 0.75f
}
