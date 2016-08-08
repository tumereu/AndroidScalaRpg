package com.tume.scalarpg.model.property

import com.tume.engine.util.Calc
import com.tume.scalarpg.model.property.HealType.HealType

/**
  * Created by tume on 5/18/16.
  */
class Healing(val amount: Float, val healType: HealType) {
  def cleanAmount: String = if (amount == 0) "0" else Calc.max(1, Calc.round(amount)).toString
}
object HealType extends Enumeration {
  type HealType = Value
  val Health, Mana = Value
}
