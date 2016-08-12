package com.tume.scalarpg.model

import android.util.Log
import com.tume.engine.anim.ClampedLinearSpikeAnim
import com.tume.engine.gui.model.UIModel
import com.tume.engine.util.{Rand, Calc, Bitmaps}
import com.tume.scalarpg.model.hero.HeroClass
import com.tume.scalarpg.model.item.EquipSlot.EquipSlot
import com.tume.scalarpg.model.item.EquipSlot.EquipSlot
import com.tume.scalarpg.model.item.{IncreasedHealthAffix, EquipSlot, Equipment}
import com.tume.scalarpg.model.potion.{ManaPotion, HealthPotion, Potion}
import com.tume.scalarpg.model.property.Damage
import com.tume.scalarpg.model.property.Stat._
import com.tume.scalarpg.{R, TheGame}
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.model.property.Element._

import scala.collection.mutable

/**
  * Created by tume on 5/13/16.
  */
class Hero(game: TheGame) extends Creature(game) {

  this.bitmap = Some(Bitmaps.get(R.drawable.hero_warrior))

  var equipment = mutable.Map[EquipSlot, Equipment]()

  var heroClass = new HeroClass()

  var statFactors = mutable.Map[Stat, Float]().withDefault(a => 1f)

  var level = 1
  var xp = 0
  def speed = 5f
  def attackSpeed = 1.5f

  override def maxHealth = standardScaling * heroClass.baseHealth * statFactors(HealthFactor)
  override def maxMana = logarithmicScaling * heroClass.baseMana * statFactors(ManaFactor)
  def reqXp = (Math.pow(level, 1.4) * 100).toInt

  this.health = maxHealth
  this.mana = maxMana

  def standardScaling = (5 + level * (level + 1) / 2).toFloat / 6
  def logarithmicScaling = Calc.log(level + 5) / Calc.log(6)

  var potions = Vector.empty[Potion] :+ new HealthPotion :+ new HealthPotion :+ new ManaPotion

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
          animLoc = Some(tile.get.loc)
          moveAnim = ClampedLinearSpikeAnim(0.15f, 5f)
        }
      }
    } else {
      // Interact with items in the tile
      for (o <- currentTile.get.objects) {
        o match {
          case potion: Potion => pickupPotion(potion)
          case _ =>
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

  override def calculateBasicAttackDamage = Damage(Rand.f(standardScaling * 2, standardScaling * 4), Physical)

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

  def potionAmount(potionType: Class[_ <: Potion]): Int = potions.count(_.getClass == potionType)

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

  def createStartingEquipment(): Unit = {
    this.equipment += EquipSlot.Helmet -> Equipment.generateArmor(1, theGame, EquipSlot.Helmet)
    this.equipment += EquipSlot.Body -> Equipment.generateArmor(1, theGame, EquipSlot.Body)
    this.equipment += EquipSlot.Boots -> Equipment.generateArmor(1, theGame, EquipSlot.Boots)
    this.equipment += EquipSlot.MainHand -> Equipment.generateWeapon(1, theGame)
    this.equipment += EquipSlot.OffHand -> Equipment.generateWeapon(1, theGame)
    this.equipment += EquipSlot.Trinket -> Equipment.generateTrinket(1, theGame)

    this.calculateEquipmentStats()
  }

  def calculateEquipmentStats(): Unit = {
    this.statFactors = mutable.Map[Stat, Float]().withDefault(a => 1f)
    for (eq <- this.equipment.values) {
      for (a <- eq.affixes) {
        statFactors(a.stat) = statFactors(a.stat) + a.amount
      }
    }
  }

}
