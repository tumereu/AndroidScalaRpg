package com.tume.scalarpg.model.item

import com.tume.engine.util.{Rand, Calc}

/**
  * Created by tume on 8/12/16.
  */
trait EquipmentAffix {
  private val scalingStart = 10f

  var variation = Rand.f(0.8f, 1.2f)
  def itemLevel: Int

  def standardScaling = ((scalingStart + Calc.pow(itemLevel, 1.3f)) * variation) / (scalingStart + 1)
  def logScaling = Math.log(scalingStart * itemLevel * variation) / Math.log(scalingStart + 1)
  def tooltipLine : String
}
