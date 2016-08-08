package com.tume.scalarpg.model.hero

import com.tume.engine.model.RichString
import com.tume.scalarpg.model.hero.TargetingType.TargetingType

/**
  * Created by tume on 8/8/16.
  */
class Ability {
  var name = ""
  var description = Vector.empty[RichString]
  var icon = 0
  var manaCost = 0f
  var healthCost = 0f
  var cooldown = 0f
  var targetingType : TargetingType = TargetingType.Self
  var range = 0f
  var areaRadius = 0f
}
object TargetingType extends Enumeration {
  type TargetingType = Value
  val Self, Toggle, Creature, Ally, Enemy, Tile, Global = Value
}