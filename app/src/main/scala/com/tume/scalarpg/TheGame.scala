package com.tume.scalarpg

import android.graphics.{Canvas, Paint}
import android.util.Log
import com.tume.engine.Game
import com.tume.engine.gui.UIView
import com.tume.engine.gui.event.UIEvent
import com.tume.engine.util.{Bitmaps, DisplayUtils}
import com.tume.scalarpg.model.{Creature, Tile}
import com.tume.scalarpg.ui.{Drawables, GameUI}

/**
  * Created by tume on 5/11/16.
  */
class TheGame extends Game {

  var floor = Map[(Int, Int), Tile]()
  var floorWidth, floorHeight = 0

  def createFloor(): Unit = {
    floor = Map[(Int, Int), Tile]()
    floorWidth = 6
    floorHeight = 6
    for (x <- 0 until floorWidth; y <- 0 until floorHeight) {
      floor += (x, y) -> new Tile(x, y, Bitmaps.random(Drawables.floorsSandStone))
    }
    floor((3,4)).addObject(new Creature())
  }

  def update(delta: Double): Unit = {

  }

  def render(canvas: Canvas): Unit = {
    // Reset the canvas
    canvas.drawColor(0xFF000000)

    // Scale the map so it fits on the whole screen
    val mapWidth = floorWidth * Tile.size
    val scaling = canvas.getWidth.toFloat / mapWidth
    canvas.scale(scaling, scaling)
    // Draw the floor
    for (x <- 0 until floorWidth; y <- 0 until floorHeight) {
      canvas.translate(Tile.size * x, Tile.size * y)
      floor((x, y)).render(canvas)
      canvas.translate(Tile.size * -x, Tile.size * -y)
    }
    // Draw the objects
    for (x <- 0 until floorWidth; y <- 0 until floorHeight) {
      canvas.translate(Tile.size * x, Tile.size * y)
      floor((x, y)).renderObjects(canvas)
      canvas.translate(Tile.size * -x, Tile.size * -y)
    }
    // Reset the scaling
    canvas.scale(1F / scaling, 1F / scaling)

    // Create a paint for drawing the grid
    val gridPaint = new Paint()
    gridPaint.setColor(0xff000000)
    gridPaint.setStrokeWidth(DisplayUtils.scale)
    for (x <- 0 until floorWidth) {
      canvas.drawLine(x * Tile.size * scaling, 0, x * Tile.size * scaling, floorHeight * Tile.size * scaling, gridPaint)
    }
    for (y <- 0 until floorWidth) {
      canvas.drawLine(0, y * Tile.size * scaling, floorWidth * Tile.size * scaling, y * Tile.size * scaling, gridPaint)
    }
  }

  override def views: Seq[UIView] = Vector(new GameUI())

  override def onUIEvent(event: UIEvent): Unit = {
    Log.d("TAG", event.view.get + " " + event.id.get)
  }

  override def init(): Unit = {
    createFloor()
  }
}
