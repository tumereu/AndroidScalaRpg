package com.tume.scalarpg.model.item

/**
  * Created by tume on 5/19/16.
  */
object EquipSlot extends Enumeration {
  type EquipSlot = Value
  val Weapon, Helmet, Body, Boots, Trinket = Value
  val MainHand, OffHand = Value
}
object WeaponCategory extends Enumeration {
  type WeaponCategory = Value
  val Sword, Axe, Mace, Dagger = Value
  val GreatSword = Value("Greatsword")
  val GreatAxe = Value("Greataxe")
  val GreatHammer = Value("Great hammer")
  val Shield, Focus, Staff, Bow = Value
}
