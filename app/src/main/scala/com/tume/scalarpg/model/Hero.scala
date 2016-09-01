package com.tume.scalarpg.model

import android.util.Log
import com.tume.engine.anim.ClampedLinearSpikeAnim
import com.tume.engine.util.{L, Rand, Calc, Bitmaps}
import com.tume.scalarpg.model.hero.HeroClass
import com.tume.scalarpg.model.item.EquipSlot.EquipSlot
import com.tume.scalarpg.model.item.EquipSlot._
import com.tume.scalarpg.model.item.Weapon
import com.tume.scalarpg.model.item._
import com.tume.scalarpg.model.potion.{ManaPotion, HealthPotion, Potion}
import com.tume.scalarpg.model.property.{BasicAttack, Damage}
import com.tume.scalarpg.model.property.Stat._
import com.tume.scalarpg.{R, TheGame}
import com.tume.scalarpg.model.Direction.Direction
import com.tume.scalarpg.model.property.Elements._

import scala.collection.mutable

/**
  * Created by tume on 5/13/16.
  */
class Hero(game: TheGame, val heroClass: HeroClass) extends Creature(game) {

  this.bitmap = Some(Bitmaps.get(heroClass.icon))

  var equipment = mutable.Map[EquipSlot, Equipment]().withDefault(s => new WoodenEquipment(s, game))

  var statFactors = mutable.Map[Stat, Float]().withDefault(a => 1f)
  var attacks = Vector.empty[BasicAttack]
  var resistances = mutable.Map[Element, Int]().withDefault(a => 0)
  var maxResistances = mutable.Map[Element, Int]().withDefault(a => 90)

  var attackCounter = 0

  var level = 1
  var xp = 0
  def abilityPower = (heroClass.baseAbilityPower + statFactors(AbilityPower)) * statFactors(AbilityPowerFactor)
  def movementSpeed = heroClass.speed
  def attackSpeed = heroClass.attackSpeed * statFactors(AttackSpeedFactor)

  override def maxHealth = standardScaling * heroClass.baseHealth * statFactors(HealthFactor)
  override def maxMana = logarithmicScaling * heroClass.baseMana * statFactors(ManaFactor)
  def reqXp = (Math.pow(level, 1.4) * 100).toInt

  this.health = maxHealth
  this.mana = maxMana

  def standardScaling = (5 + level * (level + 1) / 2).toFloat / 6
  def logarithmicScaling = Calc.log(level + 5) / Calc.log(6)

  var potions = Vector.empty[Potion] :+ new HealthPotion :+ new HealthPotion :+ new ManaPotion

  calculateEquipmentStats()

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
          game.playerActionDone(1f / attackSpeed)
          animLoc = Some(tile.get.loc)
          moveAnim = ClampedLinearSpikeAnim(0.15f, 5f)
          attackCounter += 1
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
      game.playerActionDone(1f / movementSpeed)
    }
    false
  }

  def pickupPotion(potion: Potion): Unit = {
    potions = potions :+ potion
    theGame.removeObject(potion)
  }

  def update(delta: Double): Unit = { }

  override def calculateBasicAttackDamage : Damage = {
    val a = this.attacks(attackCounter % attacks.length)
    Damage(Rand.f(standardScaling * a.minDamage, standardScaling * a.maxDamage), Physical)
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
    this.calculateEquipmentStats()
  }

  def equipItem(item: Equipment, mainSlot: Boolean = true): Unit = {
    import EquipSlot._

    item match {
      case w: Weapon => {
        if (w.twoHanded ||
          (equipment(MainHand).isInstanceOf[Weapon] && equipment(MainHand).asInstanceOf[Weapon].twoHanded) ||
          (equipment(OffHand).isInstanceOf[Weapon] && equipment(OffHand).asInstanceOf[Weapon].twoHanded)) {
          this.equipment.remove(MainHand)
          this.equipment.remove(OffHand)
        }
        if (mainSlot) this.equipment(MainHand) = w
        else this.equipment(OffHand) = w
      }
      case e: Equipment => this.equipment(e.equipSlot) = e
    }
    calculateEquipmentStats()
  }

  def calculateEquipmentStats(): Unit = {
    this.statFactors = mutable.Map[Stat, Float]().withDefault(a => 1f)
    statFactors(AbilityPower) = 0f
    this.resistances = mutable.Map[Element, Int]().withDefault(a => 0)
    this.maxResistances = mutable.Map[Element, Int]().withDefault(a => 90)
    for (eq <- this.equipment.values) {
      for (a <- eq.affixes) {
        statFactors(a.stat) = statFactors(a.stat) + a.amount
      }
      for (a <- eq.resistances) {
        resistances(a._1) = Calc.min(resistances(a._1) + a._2, maxResistances(a._1))
      }
    }
    attacks = Vector.empty[BasicAttack]
    equipment(MainHand) match {
      case w: Weapon if w.isWeapon => addBasicAttack(w)
      case _ =>
    }
    equipment(OffHand) match {
      case w: Weapon if w.isWeapon => addBasicAttack(w)
      case _ =>
    }
    def addBasicAttack(w: Weapon): Unit = {
      var min, max, acc, crit = 0f
      w.implicitAffixes.foreach {
        case ImplicitMeleeAccuracy(f) => acc = f
        case ImplicitMeleeCritChance(f) => crit = f
        case i: ImplicitMeleeDamage => min = i.range.min; max = i.range.max;
      }
      min *= statFactors(BaseDamageFactor)
      max *= statFactors(BaseDamageFactor)
      crit *= statFactors(AttackCritFactor)
      acc = 1f - ((1f - acc) / statFactors(AccuracyFactor))
      attacks = attacks :+ new BasicAttack(min, max, crit, acc)
    }
    if (attacks.isEmpty) { // No weapons, we create an attack with our fists
      val min = 1f * statFactors(BaseDamageFactor)
      val max = 2f * statFactors(BaseDamageFactor)
      val crit = 0.005f * statFactors(AttackCritFactor)
      val acc = 1f - (0.75f / statFactors(AccuracyFactor))
      attacks = attacks :+ new BasicAttack(min, max, crit, acc)
    } else if (attacks.size == 2) { // Dual wielding increases attack speed by 30%
      statFactors(AttackSpeedFactor) *= 1.3f
    }
  }

}
