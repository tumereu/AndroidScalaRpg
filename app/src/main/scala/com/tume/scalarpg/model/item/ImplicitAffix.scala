package com.tume.scalarpg.model.item

import com.tume.engine.util.Calc
import com.tume.scalarpg.model.property.DamageRange

/**
  * Created by tume on 8/12/16.
  */
trait ImplicitAffix extends EquipmentAffix {
  def itemLevel = 1
}
case class ImplicitMeleeDamage(val fMin: Float, val fMax: Float, val il: Int) extends ImplicitAffix {
  override def itemLevel = il
  def range = DamageRange(fMin * standardScaling * 2, fMax * standardScaling * 3.5f)
  override def tooltipLine: String = range.cleanAmount + " dmg"
}
case class ImplicitMeleeAccuracy(val f: Float) extends ImplicitAffix {
  val acc = f * variation
  override def tooltipLine: String = Calc.clean(f * 100, 0) + "% accuracy"
}
case class ImplicitMeleeCritChance(val f: Float) extends ImplicitAffix {
  override def tooltipLine: String = Calc.clean(f * 100, 1) + "% crit chance"
}
