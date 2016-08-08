package com.tume.scalarpg.model

import com.tume.engine.anim.{ClampedLinearSpikeAnim, LinearAnim, Animation}
import com.tume.engine.model.Vec2
import com.tume.engine.util.{Rand, Calc, Bitmaps}
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.model.property.{Healing, Damage}
import com.tume.scalarpg.model.property.HealType._
import com.tume.scalarpg.model.property.Element._
import com.tume.scalarpg.{TheGame, R}

/**
  * Created by tume on 5/12/16.
  */
class Creature(val theGame: TheGame) extends TileObject {

  var animLoc = Option[Vec2](null)
  var moveAnim = Animation()

  def maxHealth = 0f
  def maxMana = 0f

  var health, mana = 0f

  this.bitmap = Some(Bitmaps.get(R.drawable.dwarf))

  def move(dir: Direction): Boolean = {
    val tile = theGame.tileAt(Direction.atCoordinates(currentTile.get.location, dir))
    if (tile.isDefined && tile.get.canBeEntered) {
      animLoc = Some(Vec2(currentTile.get.x, currentTile.get.y))
      moveAnim = LinearAnim(0.065f)
      this.currentTile.foreach(_.removeObject(this))
      tile.foreach(_.addObject(this))
      true
    } else {
      false
    }
  }

  override def offSet: Vec2 = moveAnim match {
    case c: ClampedLinearSpikeAnim => (currentTile.get.loc.lerp(animLoc.get, moveAnim.value / 2) - currentTile.get.loc) * Tile.size
    case _ =>  if (animLoc.isEmpty) Vec2.zero else (animLoc.get.lerp(currentTile.get.loc, moveAnim.value) - currentTile.get.loc) * Tile.size
  }

  def calculateBasicAttackDamage: Damage = Damage(Rand.f(2f), Physical)

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
