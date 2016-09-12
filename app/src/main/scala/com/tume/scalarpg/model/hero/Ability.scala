package com.tume.scalarpg.model.hero

import com.tume.engine.gui.model.UIModel
import com.tume.scalarpg.model.Hero
import com.tume.scalarpg.model.hero.TargetingType.TargetingType

/**
  * Created by tume on 8/8/16.
  */
class Ability extends UIModel {
  import com.tume.scalarpg.model.hero.LosType._

  protected val Unlimited = 10000f

  var user = Option[Hero](null)

  var name = ""
  def description = ""
  var ic = 0
  var manaCost = 0f
  var healthCost = 0f
  var cooldown = 0f
  var targetingType : TargetingType = TargetingType.Self
  var range = 0f
  var areaRadius = 0f
  var losType: LosType = NothingBlocks

  var dmgFactor = 0f
  var powerFactor = 0f

  override def icon = Some(ic)
  override def tooltip = Some(name + " \n\n" + description)

  def power = user.get.currentAttack.range.avg * dmgFactor + user.get.abilityPower * powerFactor
}
object TargetingType extends Enumeration {
  type TargetingType = Value
  val Self, Toggle, Creature, Ally, Enemy, Tile, EmptyTile, Global = Value
}
object LosType extends Enumeration {
  type LosType = Value
  val EverythingBlocks, UnpassablesBlock, WallsBlock, NothingBlocks = Value
}