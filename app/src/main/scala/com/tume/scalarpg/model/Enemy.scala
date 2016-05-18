package com.tume.scalarpg.model

import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.{R, TheGame}

/**
  * Created by tume on 5/13/16.
  */
class Enemy(game: TheGame) extends Creature(game) {

  override def maxHealth = 20
  override def maxMana = 5
  this.health = maxHealth
  this.mana = maxMana

  var xp = 20

  this.bitmap = Some(Bitmaps.get(R.drawable.crocodile))

  def attackCreature(creature: Creature): Unit = {
    val dmg = calculateBasicAttackDamage
    game.addEnemyToPlayerDamageObject(dmg, this.currentTile.get)
  }

  override def roundEnded(game: TheGame): Unit = {
    super.roundEnded(game)
    this.attackCreature(game.player)
  }

}
