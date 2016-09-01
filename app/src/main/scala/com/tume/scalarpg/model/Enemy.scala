package com.tume.scalarpg.model

import com.tume.engine.anim.{LoopType, QuinticOutAnim, Animation, EmptyAnim}
import com.tume.engine.util.{Rand, L, Timer, Bitmaps}
import com.tume.scalarpg.model.property.Elements._
import com.tume.scalarpg.model.property.{Elements, Damage, DamageRange}
import com.tume.scalarpg.{R, TheGame}

/**
  * Created by tume on 5/13/16.
  */
class Enemy(val game: TheGame, val property: EnemyProperty) extends Creature(game) {

  val elite = Rand.f < 0.1f

  val element = if (!elite) property.dmg.element else Rand.from(Elements.all)

  def healthFactor = if (elite) 5f else 1f
  def dmgFactor = if (elite) 2.5f else 1f
  def sizeFactor = if (elite) 1.25f else 1f

  var spawnAnimation = Animation()

  var attackTimer = new Timer(property.attackSpeed)
  override def maxHealth = property.hp * healthFactor
  override def maxMana = property.mana * healthFactor
  this.health = maxHealth
  this.mana = maxMana

  override def relativeSize = spawnAnimation.value(property.size) * sizeFactor

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

  override def calculateBasicAttackDamage: Damage = Damage(Rand.f(property.dmg.min * dmgFactor, property.dmg.max * dmgFactor), element)

  override def turnEnded(game: TheGame, timeDelta: Float): Unit = {
    super.turnEnded(game, timeDelta)
    if (attackTimer.tick(timeDelta)) {
      this.attackCreature(game.player)
    }
  }

}
class EnemyProperty(val difficulty: Int, val name: String, val ic: Int, val hp: Int, val mana: Int = 0, val dmg: DamageRange, val attackSpeed : Float,
                    val xp: Int, val size: Float = TileObject.relativeSize) {}