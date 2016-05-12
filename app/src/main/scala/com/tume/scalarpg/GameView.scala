package com.tume.scalarpg

import android.content.Context
import android.graphics.{BitmapFactory, Paint, Canvas}
import android.util.AttributeSet
import android.view.{MotionEvent, SurfaceView, View}
import com.tume.scalarpg.util.Input

class GameView(ctx: Context, atSet: AttributeSet) extends SurfaceView(ctx, atSet) {
  val surfaceHolder = getHolder

  override def onTouchEvent(motionEvent: MotionEvent): Boolean = {
    Input.addEvent(motionEvent)
    true
  }
}
