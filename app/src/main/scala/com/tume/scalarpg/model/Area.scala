package com.tume.scalarpg.model

import com.tume.engine.gui.model.UIModel
import com.tume.scalarpg.R
import com.tume.scalarpg.model.property.DamageRange
import com.tume.scalarpg.ui.Drawables

/**
  * Created by tume on 8/27/16.
  */
class Area extends UIModel {

  var name = "Area"
  var description = Vector.empty[String]
  var menuImage = -1
  var types = Vector.empty[EnemyProperty]
  var floorDrawables = Array.empty[Int]
  var wallDrawables = Array.empty[Int]
  var width, height = 6
  var itemLevel = 0
  var wallAmount = 4 -> 7

  def byDanger(danger: Int) = types.filter(_.difficulty <= danger).last

  override def icon = Some(menuImage)
}
object Areas {
  val areas = Vector(new Area() {
    name = "Desert temple"
    menuImage = R.drawable.desert_temple
    description = Vector("-Boss deals physical damage")
    itemLevel = 1
    types = Vector[EnemyProperty](
      new EnemyProperty(0, "Green lizard", R.drawable.en_green_lizard, hp=6, mana=0, DamageRange(0.5f,1.5f), 4f, xp=20, size=0.6f),
      new EnemyProperty(18, "Alligator", R.drawable.en_alligator, hp=12, mana=0, DamageRange(5,7), 5f, xp=50, size=0.75f),
      new EnemyProperty(45, "Merman brute", R.drawable.en_merfolk_plain, hp=50, mana=0, DamageRange(12,20), 4f, xp=150, size=0.7f)
    ).sortBy(_.difficulty)
    floorDrawables = Drawables.floorsSandStone
    wallDrawables = Drawables.wallsStoneBrown
    width = 6
    height = 6
    wallAmount = 3 -> 5
  },
    new Area() {
      name = "Temple dungeon"
      menuImage = R.drawable.dungeon1
      description = Vector("-Some enemies deal arcane damage", "-More traps", "-Boss deals fire & physical damage")
      itemLevel = 10
      types = Vector[EnemyProperty](
        new EnemyProperty(0, "Green lizard", R.drawable.en_green_lizard, hp=20, mana=0, DamageRange(0,1.5f), 6f, xp=20, size=0.6f),
        new EnemyProperty(18, "Alligator", R.drawable.en_alligator, hp=30, mana=0, DamageRange(5,8), 7f, xp=50, size=0.75f),
        new EnemyProperty(45, "Merman brute", R.drawable.en_merfolk_plain, hp=65, mana=0, DamageRange(12,15), 5f, xp=150, size=0.7f)
      ).sortBy(_.difficulty)
      floorDrawables = Drawables.floorsMarble
      wallDrawables = Drawables.wallsStoneBrown
      width = 7
      height = 7
      wallAmount = 5 -> 8
    })
}
