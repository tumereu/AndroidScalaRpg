package com.tume.scalarpg.model.property

/**
  * Created by tume on 9/1/16.
  */
case class BasicAttack(var minDamage: Float, var maxDamage: Float, var critChance: Float, var accuracy: Float) {
  def range = DamageRange(minDamage, maxDamage)
}
