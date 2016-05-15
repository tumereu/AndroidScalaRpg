package com.tume.scalarpg.ui

import android.graphics.Canvas
import com.tume.engine.gui.UIComponent
import com.tume.scalarpg.TheGame
import com.tume.scalarpg.model.Tile

/**
  * Created by tume on 5/13/16.
  */
class GameCanvas extends UIComponent {

  var game: Option[TheGame] = None

  override def render(canvas: Canvas): Unit = {
    if (game.isDefined) {
      val g = game.get
      // Reset the canvas
      canvas.drawColor(0xFF000000)

      // Scale the map so it fits on the whole screen
      canvas.translate(translateX, translateY)
      canvas.scale(scaling, scaling)
      // Draw the floor
      for (x <- 0 until g.floorWidth; y <- 0 until g.floorHeight) {
        canvas.translate(Tile.size * x, Tile.size * y)
        g.floor((x, y)).render(canvas)
        canvas.translate(Tile.size * -x, Tile.size * -y)
      }
      // Draw the objects
      for (x <- 0 until g.floorWidth; y <- 0 until g.floorHeight) {
        canvas.translate(Tile.size * x, Tile.size * y)
        g.floor((x, y)).renderObjects(canvas)
        canvas.translate(Tile.size * -x, Tile.size * -y)
      }
      // Reset the scaling
      canvas.scale(1F / scaling, 1F / scaling)
      canvas.translate(-translateX, -translateY)
    }
  }

  def mapWidth = game.get.floorWidth * Tile.size
  def mapHeight = game.get.floorHeight * Tile.size
  def scaling = Math.min(width.toFloat / (game.get.floorWidth * Tile.size), height.toFloat / (game.get.floorHeight * Tile.size))
  def translateX = width.toFloat / 2 - (mapWidth * scaling / 2)
  def translateY = height.toFloat / 2 - (mapHeight * scaling / 2)

  def coordinatesForLocation(loc: (Int, Int)) : (Float, Float) = {
    (x + translateX + Tile.size * (loc._1 + 0.5f) * scaling, y + translateY + Tile.size * (loc._2 + 0.5f) * scaling)
  }

  def coordinatesForLocation(tile: Tile) : (Float, Float) = coordinatesForLocation((tile.x, tile.y))
}
