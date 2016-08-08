package com.tume.scalarpg.model.property

import com.tume.scalarpg.model.property.Element._

/**
  * Created by tume on 5/13/16.
  */
case class Damage(val amount: Float, val element: Element) {
  def cleanAmount = Math.max(1, Math.round(amount)).toString
}
case class DamageRange(val min: Float, val max: Float, val element: Element = Physical) {
  def cleanAmount = min.round + "-" + max.round
}
