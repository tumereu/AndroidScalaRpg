package com.tume.scalarpg

import com.tume.engine.{Game, GameActivity}

class MainActivity extends GameActivity {
  override def createGame: Game = new TheGame()
}
