package com.tume.scalarpg.model.potion

import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.R
import com.tume.scalarpg.model.Hero

/**
  * Created by tume on 5/17/16.
  */
class ExperiencePotion extends Potion {
  this.bitmap = Some(Bitmaps.get(R.drawable.potion_emerald))

  override def quaff(player: Hero): Unit = {
    player.gainXp(player.reqXp / 5)
  }
}
