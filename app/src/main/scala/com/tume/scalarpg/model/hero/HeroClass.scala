package com.tume.scalarpg.model.hero

import com.tume.engine.model.RichString

/**
  * Created by tume on 8/8/16.
  */
class HeroClass {
  var name = ""
  var icon = 0
  var abilities = Vector.empty[Ability]
  var description = Vector.empty[RichString]
}
