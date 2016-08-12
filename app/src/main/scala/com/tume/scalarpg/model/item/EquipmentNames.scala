package com.tume.scalarpg.model.item

import com.tume.engine.util.Rand
import com.tume.scalarpg.model.item.WeaponCategory._

/**
  * Created by tume on 8/12/16.
  */
object EquipmentNames {

  val helmets = Array[String]("Helmet", "Hat", "Cap", "Protector")
  val boots = Array[String]("Boots", "Shoes", "Steppers")
  val bodyArmors = Array[String]("Garb", "Veil", "Protector", "Chain", "Plate")
  val trinkets = Array[String]("Amulet", "Jewel", "Trinket")

  def random(a: Array[String]): String = Rand.from(a)

}
