package com.tume.engine

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.{View, Window, WindowManager}
import com.tume.engine.effect.EffectSystem
import com.tume.engine.gui.UISystem
import com.tume.engine.util.{DisplayUtils, Bitmaps}
import com.tume.scalaengine.R

abstract class GameActivity extends AppCompatActivity {

  protected var game: Game = null
  protected var ui: UISystem = null
  protected var effects: EffectSystem = null
  protected var loop: GameLoop = null

  override protected def onCreate(savedInstance: Bundle): Unit = {
    super.onCreate(savedInstance)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    requestWindowFeature(Window.FEATURE_ACTION_BAR)
    getSupportActionBar.hide()
    getWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN)
    getWindow.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    setContentView(R.layout.game_activity)
    val gameView = findViewById(R.id.gameView)
    val game = createGame
    game.gameView = gameView.asInstanceOf[GameView]
    init(game)
  }

  def createGame: Game

  def init(game: Game): Unit = {
    Bitmaps.context = Some(this)
    DisplayUtils.init(this)

    this.game = game

    ui = new UISystem()
    ui.init(game.views, game)
    game.uiSystem = ui

    effects = new EffectSystem()
    game.effectSystem = effects

    game.init()

    loop = new GameLoop(game, findViewById(R.id.gameView).asInstanceOf[GameView])
  }

  override def onPause(): Unit = {
    super.onPause()
    this.loop.pause()
  }

  override def onResume(): Unit = {
    super.onResume()
    val thread = new Thread(loop)
    thread.start()
  }

}
