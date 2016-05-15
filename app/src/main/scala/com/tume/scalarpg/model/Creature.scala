package com.tume.scalarpg.model

import android.util.Log
import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.model.property.Damage
import com.tume.scalarpg.model.property.Element._
import com.tume.scalarpg.{TheGame, R}

/**
  * Created by tume on 5/12/16.
  */
class Creature(val theGame: TheGame) extends TileObject {

  var health, maxHealth, mana, maxMana = 0D

  this.bitmap = Some(Bitmaps.get(R.drawable.dwarf))

  def move(dir: Direction): Unit = {
    val tile = theGame.tileAt(Direction.atCoordinates(currentTile.get.location, dir))
    if (tile.isDefined && tile.get.canBeEntered) {
      this.currentTile.foreach(_.removeObject(this))
      tile.foreach(_.addObject(this))
    }
  }

  def calculateBasicAttackDamage: Damage = {
    new Damage(Math.random() * 2, Physical)
  }

  def takeDamage(damage: Damage) = {
    this.health -= damage.amount
    if (health < 0) {
      health = 0
    }
  }

  override def isPassable = false

}
