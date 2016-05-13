package com.tume.engine

import android.content.Context
import android.util.AttributeSet
import android.view.{MotionEvent, SurfaceView}
import com.tume.engine.util.Input

class GameView(ctx: Context, atSet: AttributeSet) extends SurfaceView(ctx, atSet) {
  val surfaceHolder = getHolder

  override def onTouchEvent(motionEvent: MotionEvent): Boolean = {
    Input.addEvent(motionEvent)
    true
  }
}
