package com.tume.scalarpg.model.item

import com.tume.scalarpg.model.item.EquipSlot._
import com.tume.scalarpg.model.property.{DamageRange, Healing, Damage}

/**
  * Created by tume on 5/19/16.
  */
sealed trait EquipmentAffix {

  private val scalingStart = 10D

  var itemLevel = 1
  var variation = 1D

  def weight(equipSlot: EquipSlot) : Int
  def tooltipLine : String
  def amount: Double

  def factor = 1 + amount
  def percent = (amount * 100).round + "%"

  def modifyOutgoingDamage(damage: Damage) : Damage = damage
  def modifyIncomingDamage(damage: Damage) : Damage = damage

  def modifyIncomingHealing(healing: Healing) : Healing = healing

  def modifyBaseDamage(damage: DamageRange) : DamageRange = damage
  def modifyAbilityPower(power: Double) : Double = power

  def modifyHealth(health: Double) : Double = health
  def modifyMana(mana: Double) : Double = mana
  def modifySpeed(speed: Double) : Double = speed
  def modifyAttackSpeed(speed: Double) : Double = speed
  def modifyMovementSpeed(speed: Double) : Double = speed
  def modifyExperienceGain(xp: Double) : Double = xp

  def standardScaling = (scalingStart + (itemLevel * variation + (itemLevel * variation + 1)) / 2) / (scalingStart + 1)
  def logScaling = Math.log(scalingStart * itemLevel * variation) / Math.log(scalingStart + 1)
}
case class IncreasedHealthAffix() extends EquipmentAffix {
  override def amount = 10D * standardScaling
  override def weight(equipSlot: EquipSlot) : Int = equipSlot match {
    case Weapon => 10
    case Trinket => 100
    case _ => 100
  }
  override def tooltipLine = s"+$percent health"
  override def modifyHealth(double: Double) = double * factor
}
case class IncreasedManaAffix() extends EquipmentAffix {
  override def amount = 10D * standardScaling
  override def weight(equipSlot: EquipSlot) : Int = equipSlot match {
    case Weapon => 10
    case Trinket => 150
    case _ => 50
  }
  override def tooltipLine = s"+$percent mana"
  override def modifyMana(double: Double) = double * factor
}
case class IncreasedBaseDamageAffix() extends EquipmentAffix {
  override def amount = 10D * standardScaling
  override def weight(equipSlot: EquipSlot) : Int = equipSlot match {
    case Weapon => 500
    case Trinket => 70
    case _ => 10
  }
  override def tooltipLine = s"+$percent attack damage"
  override def modifyBaseDamage(damage: DamageRange) = new DamageRange(damage.min * factor, damage.max * factor)
}