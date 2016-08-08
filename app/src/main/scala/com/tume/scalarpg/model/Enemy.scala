package com.tume.scalarpg.model

import com.tume.engine.anim.{LoopType, QuinticOutAnim, Animation, EmptyAnim}
import com.tume.engine.util.{Rand, L, Timer, Bitmaps}
import com.tume.scalarpg.model.property.Element._
import com.tume.scalarpg.model.property.{Damage, DamageRange}
import com.tume.scalarpg.{R, TheGame}

/**
  * Created by tume on 5/13/16.
  */
class Enemy(val game: TheGame, val property: EnemyProperty) extends Creature(game) {

  var spawnAnimation = Animation()

  var attackTimer = new Timer(property.attackSpeed)
  override def maxHealth = property.hp
  override def maxMana = property.mana
  this.health = maxHealth
  this.mana = maxMana

  override def relativeSize = spawnAnimation.value(property.size)

  this.bitmap = Some(Bitmaps.get(property.ic))

  def attackCreature(creature: Creature): Unit = {
    val dmg = calculateBasicAttackDamage
    game.addEnemyToPlayerDamageObject(dmg, this.currentTile.get)
  }

  def spawn(): Unit = {
    spawnAnimation = QuinticOutAnim(0.4f)
  }

  override def roundEnded(game: TheGame): Unit = {
    super.roundEnded(game)
  }

  override def calculateBasicAttackDamage: Damage = Damage(Rand.f(property.dmg.min, property.dmg.max), property.dmg.element)

  override def turnEnded(game: TheGame, timeDelta: Float): Unit = {
    super.turnEnded(game, timeDelta)
    if (attackTimer.tick(timeDelta)) {
      this.attackCreature(game.player)
    }
  }

}
class EnemyProperty(val difficulty: Int, val name: String, val ic: Int, val hp: Int, val mana: Int = 0, val dmg: DamageRange, val attackSpeed : Float,
                    val xp: Int, val size: Float = TileObject.relativeSize) {}
object Enemies {
  val types = Vector[EnemyProperty](
    new EnemyProperty(0, "Green lizard", R.drawable.en_green_lizard, hp=20, mana=0, DamageRange(0,1.5f), 6f, xp=20, size=0.6f),
    new EnemyProperty(18, "Alligator", R.drawable.en_alligator, hp=30, mana=0, DamageRange(5,8), 7f, xp=50, size=0.75f),
    new EnemyProperty(45, "Merman brute", R.drawable.en_merfolk_plain, hp=65, mana=0, DamageRange(12,15), 5f, xp=150, size=0.7f)
  ).sortBy(_.difficulty)
  def byDanger(danger: Int) = types.filter(_.difficulty <= danger).last
}