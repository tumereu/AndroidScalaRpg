package com.tume.scalarpg.model

import com.tume.engine.gui.model.UIModel
import com.tume.scalarpg.R

/**
  * Created by tume on 8/27/16.
  */
class Area extends UIModel {

  var name = "Area"
  var description = Vector.empty[String]
  var menuImage = -1

  override def icon = Some(menuImage)
}
object Areas {
  val areas = Vector(new Area() {
    name = "Desert temple"
    menuImage = R.drawable.desert_temple
    description = Vector("-Boss deals physical damage")
  },
    new Area() {
      name = "Castle Ebonwither"
      menuImage = R.drawable.dungeon1
      description = Vector("-Some enemies deal arcane damage", "-More traps", "-Boss deals fire & physical damage")
    })
}
