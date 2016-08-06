package com.tume.engine

import android.graphics.Canvas
import android.util.Log
import com.tume.engine.anim.Animations

/**
  * Created by tume on 5/11/16.
  */
class GameLoop(val game: Game, val view: GameView) extends Runnable {

  val ui = game.uiSystem
  val effects = game.effectSystem

  val FPS = 50
  val MAX_SKIP = 5
  val FRAME_LENGTH = 1000 / FPS
  private var running = false

  override def run(): Unit = {
    Log.d("GameLoopInfo", "Starting game loop..")
    running = true
    while (running) {
      var canvas: Canvas = null
      try {
        canvas = view.surfaceHolder.lockCanvas()
        view.surfaceHolder.synchronized {
          val frameStart = System.currentTimeMillis()
          var framesSkipped = 0
          val fixedDelta = 1F / FPS
          update(fixedDelta)
          if (canvas != null) {
            game.background.render(canvas)
            effects.renderBelow(canvas)
            game.render(canvas)
            ui.render(canvas)
            effects.renderAbove(canvas)
          }
          val diff = System.currentTimeMillis() - frameStart
          val sleepTime = FRAME_LENGTH - diff
          if (sleepTime > 0) {
            Thread.sleep(sleepTime)
          }
          while (sleepTime + framesSkipped * FRAME_LENGTH < 0 && framesSkipped < MAX_SKIP) {
            update(fixedDelta)
            framesSkipped += 1
          }
        }
      } finally {
        if (canvas != null) {
          view.surfaceHolder.unlockCanvasAndPost(canvas)
        }
      }
    }
    Log.d("GameLoopInfo", "Stopping game loop..")
  }

  private def update(delta: Float): Unit = {
    Input.onFrameChange()
    Animations.update(delta)
    ui.update(delta)
    game.update(delta)
    effects.update(delta)
  }

  def pause(): Unit = {
    this.running = false
  }

}
