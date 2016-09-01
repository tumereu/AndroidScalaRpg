package com.tume.scalarpg.ui

import android.graphics.Paint
import com.tume.engine.util.DisplayUtils
import com.tume.scalarpg.model.item.EquipmentRarity
import com.tume.scalarpg.model.property.{Healing, Damage}
import com.tume.scalarpg.model.property.Elements._
import com.tume.scalarpg.model.property.HealType._

/**
  * Created by tume on 5/15/16.
  */
object Colors {

  def colorForElement(element: Element) : Int = element match {
    case Physical => 0xffbb0000
    case Holy => 0xffffff00
    case Fire => 0xffff3300
    case Frost => 0xff00ffff
    case Electric => 0xffccccff
    case Dark => 0xff1a001a
    case Poison => 0xff006600
    case Arcane => 0xff0000cc
  }

  def colorForHealing(healType: HealType) = healType match {
    case Health => 0xff23ba30
    case Mana => 0xff2330ba
  }

  def healPaint(healing: Healing) : Paint = {
    val paint = new Paint()
    paint.setColor(colorForHealing(healing.healType))
    paint.setTextSize(24 * DisplayUtils.scale)
    paint.setFakeBoldText(true)
    paint
  }

  def dmgPaint(damage: Damage): Paint = {
    val paint = new Paint()
    paint.setColor(colorForElement(damage.element))
    paint.setTextSize((if (damage.isCrit) 24 else 16) * DisplayUtils.scale)
    paint.setFakeBoldText(true)
    paint
  }

  def rarityPaint(equipmentRarity: EquipmentRarity): Paint = {
    val paint = new Paint()
    paint.setColor(equipmentRarity.color)
    paint.setTextSize(24 * DisplayUtils.scale)
    paint.setFakeBoldText(true)
    paint
  }

}
