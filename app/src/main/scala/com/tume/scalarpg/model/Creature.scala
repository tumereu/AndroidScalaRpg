package com.tume.scalarpg.model

import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.{TheGame, R}

/**
  * Created by tume on 5/12/16.
  */
class Creature(val theGame: TheGame) extends TileObject {

  this.bitmap = Some(Bitmaps.get(R.drawable.dwarf))

  def move(dir: Direction): Unit = {
    val tile = theGame.tileAt(Direction.atCoordinates(currentTile.get.location, dir))
    if (tile.isDefined && tile.get.canBeEntered) {
      this.currentTile.foreach(_.removeObject(this))
      tile.foreach(_.addObject(this))
    }
  }

  override def isPassable = false

}
