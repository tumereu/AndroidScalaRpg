package com.tume.scalarpg.model

import com.tume.engine.util.Bitmaps
import com.tume.scalarpg.{R, TheGame}
import com.tume.scalarpg.model.Direction.Direction

/**
  * Created by tume on 5/13/16.
  */
class Hero(game: TheGame) extends Creature(game) {

  this.bitmap = Some(Bitmaps.get(R.drawable.dwarf))

  def speed = 5f
  def attackSpeed = 1f

  this.maxHealth = 100
  this.maxMana = 100
  this.health = 100
  this.mana = 1000

  def renderedHealth = this.health
  def renderedMana = this.mana

  override def move(dir: Direction): Unit = {
    super.move(dir)
    game.playerActionDone(1f)
  }

}
