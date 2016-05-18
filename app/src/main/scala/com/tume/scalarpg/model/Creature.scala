package com.tume.scalarpg.model

import android.util.Log
import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.model.potion.Potion
import com.tume.scalarpg.model.property.{Healing, Damage}
import com.tume.scalarpg.model.property.HealType._
import com.tume.scalarpg.model.property.Element._
import com.tume.scalarpg.{TheGame, R}

/**
  * Created by tume on 5/12/16.
  */
class Creature(val theGame: TheGame) extends TileObject {

  def maxHealth = 0D
  def maxMana = 0D

  var health, mana = 0D

  this.bitmap = Some(Bitmaps.get(R.drawable.dwarf))

  def move(dir: Direction): Boolean = {
    val tile = theGame.tileAt(Direction.atCoordinates(currentTile.get.location, dir))
    if (tile.isDefined && tile.get.canBeEntered) {
      this.currentTile.foreach(_.removeObject(this))
      tile.foreach(_.addObject(this))
      true
    } else {
      false
    }
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

  def heal(healing: Healing): Unit = {
    healing.healType match {
      case Health => {
        this.health += healing.amount
        if (this.health > this.maxHealth) {
          this.health = this.maxHealth
        }
      }
      case Mana => {
        this.mana += healing.amount
        if (this.mana > this.maxMana) {
          this.mana = this.maxMana
        }
      }
    }
    theGame.addHealingObject(healing, currentTile.get)
  }

  def die(): Unit = {
    theGame.creatureDied(this)
  }

  override def isPassable = false

}
