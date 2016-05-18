package com.tume.scalarpg.model

import android.util.Log
import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.model.potion.Potion
import com.tume.scalarpg.model.property.Damage
import com.tume.scalarpg.model.property.Element._
import com.tume.scalarpg.{TheGame, R}

/**
  * Created by tume on 5/12/16.
  */
class Creature(val theGame: TheGame) extends TileObject {

  def maxHealth = 0D
  def maxMana = 0D

  var health, mana = 0D

  var potions = Vector.empty[Potion]

  this.bitmap = Some(Bitmaps.get(R.drawable.dwarf))

  def move(dir: Direction): Boolean = {
    val tile = theGame.tileAt(Direction.atCoordinates(currentTile.get.location, dir))
    if (tile.isDefined && tile.get.canBeEntered) {
      this.currentTile.foreach(_.removeObject(this))
      tile.foreach(_.addObject(this))
      // Interact with items in the tile
      for (o <- currentTile.get.objects) {
        if (o.isInstanceOf[Potion]) {
          pickupPotion(o.asInstanceOf[Potion])
        }
      }
      true
    } else {
      false
    }
  }

  def pickupPotion(potion: Potion): Unit = {
    potions = potions :+ potion
    theGame.removeObject(potion)
  }

  def calculateBasicAttackDamage: Damage = {
    new Damage(Math.random() * 2, Physical)
  }

  def takeDamage(damage: Damage) = {
    this.health -= damage.amount
    if (health < 0) {
      health = 0
      this.die()
    }
  }

  def die(): Unit = {
    theGame.creatureDied(this)
  }

  override def isPassable = false

}
