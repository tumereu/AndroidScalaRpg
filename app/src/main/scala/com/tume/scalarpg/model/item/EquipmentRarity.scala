package com.tume.scalarpg.model.item

/**
  * Created by tume on 8/22/16.
  */
sealed abstract class EquipmentRarity(val name: String, val color: Int, val resistances: Int, val affixes: Int) {
  def implicitFactor = (resistances + affixes + 2).toFloat / 7f
}
case object Common extends EquipmentRarity("Common", 0xFFFFFFFF, 3, 2)
case object Fine extends EquipmentRarity("Fine", 0xFF3366CC, 3, 3)
case object Masterwork extends EquipmentRarity("Masterwork", 0xFFFFFF00, 4, 3)
case object Enchanted extends EquipmentRarity("Enchanted", 0xFF00B300, 5, 3)
case object Mythical extends EquipmentRarity("Mythical", 0xFF993399, 5, 4)
case object Forgotten extends EquipmentRarity("Forgotten", 0xFFe62e00, 6, 4)
case object Ascended extends EquipmentRarity("Ascended", 0xFFffccff, 7, 5)