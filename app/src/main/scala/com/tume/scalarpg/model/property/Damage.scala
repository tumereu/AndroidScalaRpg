package com.tume.scalarpg.model.property

import com.tume.scalarpg.model.property.Element._

/**
  * Created by tume on 5/13/16.
  */
class Damage(val amount: Double, val element: Element) {

  def cleanAmount = Math.max(1, Math.round(amount)).toString

}
class DamageRange(val min: Double, val max: Double) {

  def cleanAmount = min.round + "-" + max.round

}
