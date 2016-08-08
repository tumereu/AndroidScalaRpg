package com.tume.scalarpg.model.potion

import android.graphics.Paint
import com.tume.engine.anim.{QuinticOutAnim, Animation}
import com.tume.scalarpg.TheGame
import com.tume.scalarpg.model.{Hero, TileObject}

/**
  * Created by tume on 5/17/16.
  */
abstract class Potion extends TileObject {

  private val startingTime = 10f
  private var timeLeft = startingTime

  var spawnAnimation = Animation()

  override def relativeSize = spawnAnimation.value(0.75f)

  def opacity = (timeLeft * 255 / startingTime).toInt
  def spawn(): Unit = {
    spawnAnimation = QuinticOutAnim(0.5f)
  }

  override def bitmapPaint: Paint = {
    val p = new Paint()
    p.setAlpha(opacity)
    p
  }

  override def roundEnded(game: TheGame): Unit = {
    super.roundEnded(game)

  }

  override def turnEnded(game: TheGame, delta: Float): Unit = {
    super.turnEnded(game, delta)
    timeLeft -= delta
    if (timeLeft <= 0) {
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
