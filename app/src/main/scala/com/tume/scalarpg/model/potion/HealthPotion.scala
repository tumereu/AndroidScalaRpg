package com.tume.scalarpg.model.potion

import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.R
import com.tume.scalarpg.model.Hero
import com.tume.scalarpg.model.property.Healing
import com.tume.scalarpg.model.property.HealType._

/**
  * Created by tume on 5/17/16.
  */
class HealthPotion extends Potion {
  this.bitmap = Some(Bitmaps.get(R.drawable.potion_ruby))

  override def quaff(player: Hero): Unit = {
    player.heal(new Healing(player.maxHealth * 0.33, Health))
  }
}
