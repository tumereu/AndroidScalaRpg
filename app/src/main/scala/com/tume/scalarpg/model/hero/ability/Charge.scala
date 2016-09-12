package com.tume.scalarpg.model.hero.ability

import com.tume.engine.util.Calc
import com.tume.scalarpg.R
import com.tume.scalarpg.model.hero.{LosType, TargetingType, Ability}

/**
  * Created by tume on 9/9/16.
  */
class Charge extends Ability {
  name = "Charge"
  override def description = "Move in a straight line to target location, causing " + Calc.clean(power, 0) + " physical damage to all enemies caught in the line"
  ic = R.drawable.hero_warrior
  manaCost = 6f
  healthCost = 0f
  cooldown = 3f
  targetingType = TargetingType.EmptyTile
  losType = LosType.WallsBlock
  range = Unlimited
  areaRadius = 0f

  dmgFactor = 1f
  powerFactor = 1f
}
