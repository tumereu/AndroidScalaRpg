package com.tume.scalarpg.model.hero

import com.tume.scalarpg.R
import com.tume.scalarpg.model.item.WeaponCategory._


/**
  * Created by tume on 8/8/16.
  */
class HeroClass {
  var name = ""
  var icon = 0
  var abilities = Vector.empty[Ability]
  var description = ""
  var baseHealth = 100
  var baseMana = 20
  var speed = 5f
  var attackSpeed = 1f
  var baseAbilityPower = 0

  var mainHandWeapons = Vector.empty[WeaponCategory]
  var offHandWeapons = Vector.empty[WeaponCategory]
}
object HeroClasses {

  val list = Vector[HeroClass](
    new HeroClass() {
      name = "Warrior"
      icon = R.drawable.hero_warrior
      description = "A fierce fighter focused on melee combat. Can wield a large variety of different weapons."
      baseHealth = 140
      baseMana = 20
      speed = 5f
      attackSpeed = 2f
      baseAbilityPower = 0
      mainHandWeapons = Vector(Sword, Axe, Mace, Dagger, GreatAxe, GreatHammer, GreatSword)
      offHandWeapons = Vector(Sword, Axe, Mace, Dagger, Shield)
    }
  )
}
