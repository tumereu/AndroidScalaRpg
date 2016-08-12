package com.tume.scalarpg.ui

import com.tume.scalarpg.R.drawable._

/**
  * Created by tume on 5/13/16.
  */
object Drawables {

  val floorsMarble = Array[Int](marble_floor1, marble_floor2, marble_floor3, marble_floor4, marble_floor5, marble_floor6)
  val floorsSandStone = Array[Int](floor_sand_stone0,floor_sand_stone1,floor_sand_stone2,
    floor_sand_stone3,floor_sand_stone4,floor_sand_stone5,floor_sand_stone6,floor_sand_stone7)

  val wallsStoneBrown = Array[Int](stone2_brown0, stone2_brown1, stone2_brown2, stone2_brown3)

  val bodyArmors = Array[Int](body_armor_0,body_armor_1,body_armor_2,body_armor_3,body_armor_4,body_armor_5,body_armor_6,body_armor_7,body_armor_8,
    body_armor_9,body_armor_10,body_armor_11,body_armor_12,body_armor_13,body_armor_14,body_armor_15,body_armor_16,body_armor_17,body_armor_18,
    body_armor_19,body_armor_20,body_armor_21,body_armor_22,body_armor_23,body_armor_24,body_armor_25,body_armor_26,body_armor_27,body_armor_28,body_armor_29)
  val boots = Array[Int](boots_0,boots_1,boots_2,boots_3,boots_4,boots_5,boots_6,boots_7)
  val helmets = Array[Int](helmet_0,helmet_1,helmet_2,helmet_3,helmet_4,helmet_5,helmet_6)

  val axes = Array[Int](axe_0,axe_1,axe_2,axe_3,axe_4,axe_5,axe_6,axe_7,axe_8,axe_9)
  val bows = Array[Int](bow_0,bow_1,bow_2,bow_3)
  val daggers = Array[Int](dagger_0,dagger_1,dagger_2,dagger_3,dagger_4,dagger_5)
  val focuses = Array[Int](focus_0,focus_1,focus_2,focus_3,focus_4,focus_5,focus_6)
  val greataxes = Array[Int](greataxe_0,greataxe_1,greataxe_2,greataxe_3,greataxe_4,greataxe_5)
  val greathammers = Array[Int](greathammer_0,greathammer_1,greathammer_2,greathammer_3,greathammer_4,greathammer_5)
  val greatswords = Array[Int](greatsword_0,greatsword_1,greatsword_2,greatsword_3,greatsword_4,greatsword_5,greatsword_6,greatsword_7)
  val maces = Array[Int](mace_0,mace_1,mace_2,mace_3,mace_4)
  val shields = Array[Int](shield_0,shield_1,shield_2,shield_3,shield_4,shield_5,shield_6,shield_7,shield_8)
  val staves = Array[Int](staff_0,staff_1,staff_2,staff_3,staff_4,staff_5,staff_6,staff_7)
  val swords = Array[Int](sword_0,sword_1,sword_2,sword_3,sword_4,sword_5,sword_6,sword_7,sword_8,sword_9,sword_10,sword_11,sword_12)


  def random(array: Array[Int]): Int = array((Math.random() * array.length).toInt)

}
