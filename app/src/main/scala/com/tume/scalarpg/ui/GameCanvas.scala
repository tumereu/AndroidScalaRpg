package com.tume.scalarpg.ui

import android.graphics.{Paint, Canvas}
import com.tume.engine.Input
import com.tume.engine.gui.{UITheme, UIComponent}
import com.tume.engine.model.Vec2
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
      // Draw los
      if (g.selectedAbility.isDefined) {
        for (x <- 0 until g.floorWidth; y <- 0 until g.floorHeight) {
          val color = g.allowedTargetsMap((x, y)) match {
            case true if g.selectedTarget.contains((x, y)) => 0x880000cc
            case true => 0x0
            case false if g.selectedTarget.contains((x, y)) => 0x88cc0000
            case false => 0x88000000
          }
          val p = UITheme.create(color, Paint.Style.FILL)
          canvas.drawRect(Tile.size * x, Tile.size * y, Tile.size * (x + 1), Tile.size * (y + 1), p)
        }
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

  def coordinatesForLocation(loc: (Int, Int)) : Vec2 = {
    Vec2(x + translateX + Tile.size * (loc._1 + 0.5f) * scaling, y + translateY + Tile.size * (loc._2 + 0.5f) * scaling)
  }

  def coordinatesForLocation(tile: Tile) : Vec2 = coordinatesForLocation((tile.x, tile.y))

  override def update(delta: Float, onTop: Boolean): Unit = {
    val t = Input.pointers.headOption
    if (t.isDefined) {
      val touch = t.get
      val tx = ((touch.x - x - (scaling * Tile.size / 2)) / (scaling * Tile.size)).toInt
      val ty = ((touch.y - y - (scaling * Tile.size / 2)) / (scaling * Tile.size)).toInt
      if (tx >= 0 && ty >= 0 && tx < game.get.floorWidth && ty < game.get.floorHeight) {
        game.get.selectedTarget = Some((tx, ty))
      } else {
        game.get.selectedTarget = None
      }
    }
  }
}
