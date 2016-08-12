package com.tume.scalarpg.model.item

import java.net.PasswordAuthentication

import android.graphics.Bitmap
import com.tume.engine.gui.model.UIModel
import com.tume.engine.util.{Bitmaps, Calc, Rand}
import com.tume.scalarpg.TheGame
import com.tume.scalarpg.model.item.EquipSlot.EquipSlot
import com.tume.scalarpg.model.item.WeaponCategory._
import com.tume.scalarpg.model.property.Element._
import com.tume.scalarpg.ui.Drawables

/**
  * Created by tume on 5/19/16.
  */
sealed abstract class Equipment(val equipSlot: EquipSlot, val id: Long) extends UIModel  {

  var name = ""
  var itemLevel = 1
  var resistances = Map[Element, Float]()
  var implicitAffixes = Vector[ImplicitAffix]()
  var affixes = Vector[NormalAffix]()
  var drawable : Int = 0

  private var description = Option[String](null)

  override def hashCode = id.hashCode()
  override def equals(o: Any) : Boolean = o match {
    case eq: Equipment => eq.id == this.id
    case _ => false
  }

  override def tooltip: Option[String] = {
    if (description.isEmpty) build()
    description
  }
  override def icon: Option[Int] = Some(drawable)

  def implicitTooltip = ""

  def build(): Unit = {
    val lb = " \n " // Line break
    var s = ""
    s += name + lb + lb
    s += implicitTooltip
    for (r <- resistances) {
      s += " +" + Calc.clean(r._2, 1) + "%" + " " + r._1 + " res" + lb
    }
    s += lb
    for (a <- affixes) {
      s += a.tooltipLine + lb
    }
    description = Some(s)
  }

}
case class Trinket(theGame: TheGame) extends Equipment(EquipSlot.Trinket, theGame.uniqueId) {
  drawable = Drawables.random(Drawables.trinkets)
  name = EquipmentNames.random(EquipmentNames.trinkets)
}
case class Weapon(theGame: TheGame, val category: WeaponCategory) extends Equipment(EquipSlot.Weapon, theGame.uniqueId) {
  name = "" + category
  drawable = Drawables.random(category match {
    case Sword => Drawables.swords
    case Mace => Drawables.maces
    case Axe => Drawables.axes
    case Dagger => Drawables.daggers
    case GreatSword => Drawables.greatswords
    case GreatAxe => Drawables.greataxes
    case GreatHammer => Drawables.greathammers
    case Staff => Drawables.staves
    case Shield => Drawables.shields
    case Focus => Drawables.focuses
    case Bow => Drawables.bows
    case Wand => Drawables.wands
  })
  override def implicitTooltip = {
    var s = ""
    if (implicitAffixes.isEmpty) {
      ""
    } else {
      for (i <- implicitAffixes) {
        s += i.tooltipLine + "\n "
      }
      s
    }
  }
}
case class Helmet(theGame: TheGame) extends Equipment(EquipSlot.Helmet, theGame.uniqueId) {
  drawable = Drawables.random(Drawables.helmets)
  name = EquipmentNames.random(EquipmentNames.helmets)
}
case class BodyArmor(theGame: TheGame) extends Equipment(EquipSlot.Body, theGame.uniqueId) {
  drawable = Drawables.random(Drawables.bodyArmors)
  name = EquipmentNames.random(EquipmentNames.bodyArmors)
}
case class Boots(theGame: TheGame) extends Equipment(EquipSlot.Boots, theGame.uniqueId) {
  drawable = Drawables.random(Drawables.boots)
  name = EquipmentNames.random(EquipmentNames.boots)
}
object Equipment {
  def generateNew(itemLevel: Int, theGame: TheGame) : Equipment = {
    val equipment = Rand.f match {
      case f: Float if f < 0.05f => generateTrinket(itemLevel, theGame)
      case f: Float if f < 0.25f => generateWeapon(itemLevel, theGame)
      case f: Float if f < 0.5f => generateArmor(itemLevel, theGame, EquipSlot.Body)
      case f: Float if f < 0.75f => generateArmor(itemLevel, theGame, EquipSlot.Boots)
      case _ => generateArmor(itemLevel, theGame, EquipSlot.Helmet)
    }
    equipment
  }

  def generateTrinket(itemLevel: Int, theGame: TheGame): Trinket = {
    val t = Trinket(theGame)
    t.affixes = t.affixes :+ IncreasedManaAffix(itemLevel)
    t
  }
  def generateWeapon(itemLevel: Int, theGame: TheGame): Weapon = {
    val category = Rand.from(Seq(
      Rand.from(Seq(Sword, Mace, Axe, Dagger)),
      Rand.from(Seq(GreatAxe,GreatHammer,GreatSword)),
      Rand.from(Seq(Shield, Focus)),
      Rand.from(Seq(Staff))
      ))
    val t = Weapon(theGame, category)
    val dmgFactor = category match {
      case Dagger => 0.8f
      case Sword | Mace | Axe => 1f
      case Bow => 0.85f
      case GreatAxe | GreatHammer | GreatSword => 1.6f
      case Shield | Focus => 0f
      case Staff | Wand => 0.6f
    }
    if (dmgFactor > 0) {
      val critChance = category match {
        case Mace | GreatHammer => 0.04f
        case GreatAxe | Axe => 0.05f
        case Sword | GreatSword => 0.065f
        case Dagger => 0.1f
        case Staff => 0.02f
        case Bow => 0.08f
        case Wand => 0.04f
      }
      val accuracy = category match {
        case Mace | Axe => 0.8f
        case Sword | GreatSword => 0.9f
        case GreatAxe | GreatHammer => 0.6f
        case Dagger => 0.95f
        case Bow => 0.4f
        case Staff => 0.6f
        case Wand => 0.8f
      }
      t.implicitAffixes = t.implicitAffixes :+ ImplicitMeleeDamage(dmgFactor, dmgFactor, itemLevel) :+
        ImplicitMeleeCritChance(critChance) :+ ImplicitMeleeAccuracy(accuracy)
    }
    t.affixes = t.affixes :+ IncreasedBaseDamageAffix(itemLevel)
    t
  }
  def generateArmor(itemLevel: Int, theGame: TheGame, slot: EquipSlot): Equipment = {
    val t = slot match {
      case EquipSlot.Helmet => Helmet(theGame)
      case EquipSlot.Boots => Boots(theGame)
      case _ => BodyArmor(theGame)
    }
    t.resistances += Physical -> Rand.f(10, 25)
    t.affixes = t.affixes :+ IncreasedHealthAffix(itemLevel)
    t
  }

}
