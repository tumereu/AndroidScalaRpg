package com.tume.scalarpg.model.property

import com.tume.scalarpg.model.property.Elements._

/**
  * Created by tume on 5/13/16.
  */
case class Damage(val amount: Float, val element: Element, isCrit: Boolean = false) {
  def cleanAmount = Math.max(1, Math.round(amount)).toString
  def crit : Damage = Damage(amount * 1.5f, element, true)
}
case class DamageRange(val min: Float, val max: Float, val element: Element = Physical) {
  def cleanAmount = min.round + "-" + max.round
}
