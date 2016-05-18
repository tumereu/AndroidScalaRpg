package com.tume.scalarpg.model.potion

import android.graphics.Paint
import com.tume.scalarpg.TheGame
import com.tume.scalarpg.model.{Hero, TileObject}

/**
  * Created by tume on 5/17/16.
  */
abstract class Potion extends TileObject {

  private val startingRounds = 3
  private var roundsLeft = startingRounds

  relativeSize = 0.75f

  def opacity = roundsLeft * 255 / startingRounds

  override def bitmapPaint: Paint = {
    val p = new Paint()
    p.setAlpha(opacity)
    p
  }

  override def roundEnded(game: TheGame): Unit = {
    super.roundEnded(game)
    roundsLeft -= 1
    if (roundsLeft == 0) {
      game.removeObject(this)
    }
  }

  def quaff(player: Hero)

}
object Potion {
  private val weightedPotions = Vector(new HealthPotion() -> 100, new ManaPotion() -> 80, new ExperiencePotion() -> 20)

  def randomPotion: Potion = {
    val total = weightedPotions.foldLeft(0)(_ + _._2)
    var random = Math.random() * total
    var pot : Option[Potion] = None
    for (pair <- weightedPotions) {
      random -= pair._2
      if (random < 0 && pot.isEmpty) {
        pot = Some(pair._1)
      }
    }
    pot.get.getClass.newInstance()
  }
}
