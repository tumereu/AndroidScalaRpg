package com.tume.scalarpg.model.property

import com.tume.scalarpg.model.property.HealType.HealType

/**
  * Created by tume on 5/18/16.
  */
class Healing(val amount: Double, val healType: HealType) {

  def cleanAmount = Math.max(1, Math.round(amount)).toString

}
object HealType extends Enumeration {
  type HealType = Value
  val Health, Mana = Value
}
