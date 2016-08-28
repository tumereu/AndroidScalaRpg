package com.tume.scalarpg.model.item

import java.net.PasswordAuthentication

import android.graphics.Bitmap
import com.tume.engine.gui.model.UIModel
import com.tume.engine.util.{L, Bitmaps, Calc, Rand}
import com.tume.scalarpg.{R, TheGame}
import com.tume.scalarpg.model.item.EquipSlot.EquipSlot
import com.tume.scalarpg.model.item.WeaponCategory._
import com.tume.scalarpg.model.property.Elements
import com.tume.scalarpg.model.property.Elements._
import com.tume.scalarpg.ui.Drawables

/**
  * Created by tume on 5/19/16.
  */
sealed abstract class Equipment(val equipSlot: EquipSlot, val id: Long) extends UIModel  {

  var name = ""
  var itemLevel = 1
  var resistances = Map[Element, Int]()
  var implicitAffixes = Vector[ImplicitAffix]()
  var affixes = Vector[NormalAffix]()
  var drawable : Int = 0
  var rarity: EquipmentRarity = Common

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
  override def bgColor : Option[Int] = Some(rarity.color)

  def implicitTooltip = ""

  def build(): Unit = {
    val lb = " \n " // Line break
    var s = ""
    s += name + lb + lb
    s += implicitTooltip
    for (r <- resistances) {
      s += " +" + r._2 + "%" + " " + r._1 + " res" + lb
    }
    if (implicitTooltip.nonEmpty || resistances.nonEmpty) {
      s += lb
    }
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
  def generateRarity(itemLevel: Int) : EquipmentRarity = {
    val offSet = 20
    Rand.i(itemLevel, itemLevel + offSet) / offSet match {
      case 0 => Common
      case 1 => Fine
      case 2 => Masterwork
      case 3 => Enchanted
      case 4 => Mythical
      case 5 => Forgotten
      case _ => Ascended
    }
  }
  def generateNew(itemLevel: Int, theGame: TheGame) : Equipment = {
    val equipment = Rand.f match {
      case f: Float if f < 0.05f => generateTrinket(itemLevel, theGame)
      case f: Float if f < 0.25f => generateWeapon(itemLevel, theGame)
      case f: Float if f < 0.5f => generateArmor(itemLevel, theGame, EquipSlot.Body)
      case f: Float if f < 0.75f => generateArmor(itemLevel, theGame, EquipSlot.Boots)
      case _ =>  generateArmor(itemLevel, theGame, EquipSlot.Helmet)
    }
    equipment
  }

  def generateTrinket(itemLevel: Int, theGame: TheGame): Trinket = {
    val t = Trinket(theGame)
    t.affixes = t.affixes :+ IncreasedManaAffix(itemLevel)
    t.rarity = generateRarity(itemLevel)
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
    t.rarity = generateRarity(itemLevel)

    val dmgFactor = (category match {
      case Dagger => 0.8f
      case Sword | Mace | Axe => 1f
      case Bow => 0.85f
      case GreatAxe | GreatHammer | GreatSword => 1.6f
      case Shield | Focus => 0f
      case Staff | Wand => 0.6f
    }) * t.rarity.implicitFactor
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
    category match {
      case Shield => {
        def addRes(element: Element): Unit = {
          val res = (Rand.f(0.4f, 0.6f) * (itemLevel + 10)).toInt
          t.resistances += element -> res
        }
        addRes(Physical)
        for (i <- 0 until t.rarity.resistances) {
          addRes(Rand.from(Elements.all.filterNot(t.resistances.contains)))
        }
      }
      case _ =>
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
    t.rarity = generateRarity(itemLevel)
    def addRes(element: Element): Unit = {
      val res = (Rand.f(0.8f, 1.2f) * (itemLevel + 10)).toInt
      t.resistances += element -> res
    }
    addRes(Physical)
    for (i <- 0 until t.rarity.resistances) {
      addRes(Rand.from(Elements.all.filterNot(t.resistances.contains)))
    }

    t.affixes = t.affixes :+ IncreasedHealthAffix(itemLevel)
    t
  }
}
class WoodenEquipment(s: EquipSlot, theGame: TheGame) extends Equipment(s, theGame.uniqueId) {
  drawable = s match {
    case EquipSlot.Helmet => R.drawable.ic_silhouette_helmet
    case EquipSlot.Body => R.drawable.ic_silhouette_armor
    case EquipSlot.Boots => R.drawable.ic_silhouette_boots
    case EquipSlot.Trinket => R.drawable.ic_silhouette_trinket
    case EquipSlot.MainHand => R.drawable.ic_silhouette_sword
    case EquipSlot.OffHand => R.drawable.ic_silhouette_shield
  }

  override def tooltip: Option[String] = None
}