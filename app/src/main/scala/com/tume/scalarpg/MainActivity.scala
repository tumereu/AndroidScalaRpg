package com.tume.scalarpg

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.{WindowManager, Window}
import com.tume.scalarpg.model.Game
import com.tume.scalarpg.util.{DisplayUtils, Bitmaps}

class MainActivity() extends AppCompatActivity {

  var game: Game = null
  var loop: GameLoop = null

  override protected def onCreate(savedInstance: Bundle): Unit = {
    super.onCreate(savedInstance)
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    requestWindowFeature(Window.FEATURE_ACTION_BAR)
    getSupportActionBar.hide()
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    setContentView(R.layout.activity_main)

    Bitmaps.context = Some(this)
    DisplayUtils.init(this)

    game = new Game()
    game.createFloor()

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
