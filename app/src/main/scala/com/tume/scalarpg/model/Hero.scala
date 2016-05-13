package com.tume.scalarpg.model

import com.tume.scalarpg.TheGame
import com.tume.scalarpg.model.Direction.Direction

/**
  * Created by tume on 5/13/16.
  */
class Hero(game: TheGame) extends Creature(game) {

  def speed = 5f
  def attackSpeed = 1f

  override def move(dir: Direction): Unit = {
    super.move(dir)
    game.playerActionDone(1f)
  }

}
