package com.tume.scalarpg.model.item

import com.tume.scalarpg.model.item.EquipSlot._
import com.tume.scalarpg.model.property.Element._

/**
  * Created by tume on 5/19/16.
  */
class Equipment {

  var name = ""
  var className = ""
  var itemLevel = 1
  var slot: EquipSlot = Weapon
  var resistances = Map[Element, Double]()

}
object Equipment {
  def generateNew(itemLevel: Int) : Equipment = {
    val equipment = new Equipment()
    val d = Math.random()
    if (d < 0.05) equipment.slot = Trinket
    else if (d < 0.25) equipment.slot = Weapon
    else if (d < 0.5) equipment.slot = Head
    else if (d < 0.75) equipment.slot = Body
    else equipment.slot = Legs
    equipment
  }
}
