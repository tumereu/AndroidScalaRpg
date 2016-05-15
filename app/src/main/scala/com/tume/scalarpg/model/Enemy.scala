package com.tume.scalarpg.model

import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.{R, TheGame}

/**
  * Created by tume on 5/13/16.
  */
class Enemy(game: TheGame) extends Creature(game) {

  this.maxHealth = 20
  this.maxMana = 15
  this.health = maxHealth
  this.mana = maxMana

  this.bitmap = Some(Bitmaps.get(R.drawable.crocodile))

  def attackCreature(creature: Creature): Unit = {
    val dmg = calculateBasicAttackDamage
    creature.takeDamage(this.calculateBasicAttackDamage)
    game.addEnemyToPlayerDamageObject(dmg, this.currentTile.get)
  }

}
