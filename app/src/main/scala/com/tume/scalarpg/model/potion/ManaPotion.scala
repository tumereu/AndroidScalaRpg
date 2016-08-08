package com.tume.scalarpg.model.potion

import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.R
import com.tume.scalarpg.model.Hero
import com.tume.scalarpg.model.property.HealType._
import com.tume.scalarpg.model.property.Healing

/**
  * Created by tume on 5/17/16.
  *
  */
case class ManaPotion() extends Potion {
  this.bitmap = Some(Bitmaps.get(R.drawable.potion_brilliant_blue))

  override def quaff(player: Hero): Unit = {
    player.heal(new Healing(player.maxMana * 0.5f, Mana))
  }
}
