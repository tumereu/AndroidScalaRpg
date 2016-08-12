package com.tume.scalarpg.model.item

import com.tume.scalarpg.model.item.EquipSlot._
import com.tume.scalarpg.model.property.Stat._
import com.tume.scalarpg.model.property.{DamageRange, Healing, Damage}

/**
  * Created by tume on 5/19/16.
  */
sealed trait NormalAffix extends EquipmentAffix {

  def weight(equipSlot: EquipSlot) : Int
  def amount: Float
  def stat: Stat
  def percent = (amount * 100).round + "%"
}
case class IncreasedHealthAffix(val itemLevel: Int) extends NormalAffix {
  override def weight(equipSlot: EquipSlot) : Int = equipSlot match {
    case EquipSlot.Weapon => 10
    case EquipSlot.Trinket => 100
    case _ => 100
  }
  override def stat = HealthFactor
  override def amount = standardScaling / 10f
  override def tooltipLine = s"+$percent health"
}
case class IncreasedManaAffix(val itemLevel: Int) extends NormalAffix {
  override def weight(equipSlot: EquipSlot) : Int = equipSlot match {
    case EquipSlot.Weapon => 10
    case EquipSlot.Trinket => 150
    case _ => 50
  }
  override def stat = ManaFactor
  override def amount = standardScaling / 10f
  override def tooltipLine = s"+$percent mana"
}
case class IncreasedBaseDamageAffix(val itemLevel: Int) extends NormalAffix {
  override def weight(equipSlot: EquipSlot) : Int = equipSlot match {
    case EquipSlot.Weapon => 500
    case EquipSlot.Trinket => 70
    case _ => 10
  }
  override def stat = BaseDamageFactor
  override def amount = standardScaling / 10f
  override def tooltipLine = s"+$percent attack damage"
}