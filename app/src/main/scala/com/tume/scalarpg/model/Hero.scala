package com.tume.scalarpg.model

import android.util.Log
import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.model.potion.Potion
import com.tume.scalarpg.model.property.Damage
import com.tume.scalarpg.{R, TheGame}
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.model.property.Element._

/**
  * Created by tume on 5/13/16.
  */
class Hero(game: TheGame) extends Creature(game) {

  this.bitmap = Some(Bitmaps.get(R.drawable.dwarf))

  var level = 1
  var xp = 0

  def reqXp = (Math.pow(level, 1.4) * 100).toInt

  def standardScaling = (5 + level * (level + 1) / 2).toDouble / 6
  def logarithmicScaling = (Math.log(level + 5)) / (Math.log(6))


  var potions = Vector.empty[Potion]

  def speed = 5f

  def attackSpeed = 1.5f

  override def maxHealth = standardScaling * 100
  override def maxMana = logarithmicScaling * 100

  this.health = maxHealth
  this.mana = maxMana

  override def move(dir: Direction): Boolean = {
    val moved = super.move(dir)
    if (!moved) {
      val tile = theGame.tileAt(Direction.atCoordinates(currentTile.get.location, dir))
      if (tile.isDefined) {
        val enemy = tile.get.objects.find(_.isInstanceOf[Enemy])
        if (enemy.isDefined) {
          val dmg = this.calculateBasicAttackDamage
          game.addPlayerToEnemyDamageObject(dmg, enemy.get.currentTile.get)
          enemy.get.asInstanceOf[Enemy].takeDamage(dmg)
          game.playerActionDone(attackSpeed)
        }
      }
    } else {
      // Interact with items in the tile
      for (o <- currentTile.get.objects) {
        if (o.isInstanceOf[Potion]) {
          pickupPotion(o.asInstanceOf[Potion])
        }
      }
      game.playerActionDone(1f)
    }
    false
  }

  def pickupPotion(potion: Potion): Unit = {
    potions = potions :+ potion
    theGame.removeObject(potion)
  }

  def update(delta: Double): Unit = { }

  override def calculateBasicAttackDamage = {
    new Damage(standardScaling * 2 + Math.random() * standardScaling * 2, Physical)
  }

  def gainXp(xp: Int): Unit = {
    this.xp += xp
    while (this.xp >=this.reqXp) {
      this.xp -= this.reqXp
      levelUp()
    }
  }

  def levelUp(): Unit = {
    val oldHealth = this.maxHealth
    val oldMana = this.maxMana
    this.level += 1
    this.health += maxHealth - oldHealth
    this.mana += maxMana - oldMana
  }

  def potionAmount(potionType: Class[_ <: Potion]): Int = potions.filter(_.getClass == potionType).size

  def quaffPotion(potionType: Class[_ <: Potion]): Unit = {
    if (potionAmount(potionType) > 0) {
      var potion: Option[Potion] = None
      this.potions = potions.filterNot(p => {
        if (p.getClass == potionType && potion.isEmpty) {
          potion = Some(p)
          true
        } else {
          false
        }
      })
      potion.get.quaff(this)
    }
  }

}
