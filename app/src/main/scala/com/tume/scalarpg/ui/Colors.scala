package com.tume.scalarpg.ui

import android.graphics.Paint
import com.tume.engine.util.DisplayUtils
import com.tume.scalarpg.model.property.{Healing, Damage}
import com.tume.scalarpg.model.property.Element._
import com.tume.scalarpg.model.property.HealType._

/**
  * Created by tume on 5/15/16.
  */
object Colors {

  def colorForElement(element: Element) : Int = element match {
    case Physical => 0xffbb0000
    case _ => 0xffffffff
  }

  def colorForHealing(healType: HealType) = healType match {
    case Health => 0xff23ba30
    case Mana => 0xff2330ba
  }

  def healPaint(healing: Healing) : Paint = {
    val paint = new Paint()
    paint.setColor(colorForHealing(healing.healType))
    paint.setTextSize(24 * DisplayUtils.scale)
    paint.setFakeBoldText(true)
    paint
  }

  def dmgPaint(damage: Damage): Paint = {
    val paint = new Paint()
    paint.setColor(colorForElement(damage.element))
    paint.setTextSize(24 * DisplayUtils.scale)
    paint.setFakeBoldText(true)
    paint
  }

}
