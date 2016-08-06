package com.tume.engine

import android.content.Context
import android.util.{Log, AttributeSet}
import android.view.{View, SurfaceHolder, MotionEvent, SurfaceView}
import com.tume.engine.util.DisplayUtils

class GameView(ctx: Context, atSet: AttributeSet) extends SurfaceView(ctx, atSet) {
  val surfaceHolder = getHolder

  override def onTouchEvent(motionEvent: MotionEvent): Boolean = {
    Input.addEvent(motionEvent)
    true
  }

  override def onSizeChanged(xNew: Int, yNew: Int, xOld: Int, yOld: Int) : Unit = {
    super.onSizeChanged(xNew, yNew, xOld, yOld)
    DisplayUtils.screenWidth = xNew
    DisplayUtils.screenHeight = yNew
    DisplayUtils.screenSize = Math.min(xNew, yNew)
  }

}
