package com.tume.scalarpg.util

import android.util.Log
import android.view.MotionEvent

import scala.collection.mutable

/**
  * Created by tume on 5/12/16.
  */
object Input {

  private var currentTouchLocation : Option[(Float, Float)] = None
  private var lastEventLoc = (-1f, -1f)
  private var touchedThisFrame = false
  private var upThisFrame = false
  private var events = mutable.Buffer[MotionEvent]()

  def touchLocation : Option[(Float, Float)] = currentTouchLocation
  def touchX : Option[Float] = if (touchLocation.isDefined) Some(touchLocation.get._1) else None
  def touchY : Option[Float] = if (touchLocation.isDefined) Some(touchLocation.get._2) else None
  def lastTouchLocation = lastEventLoc
  def down = touchedThisFrame
  def up = upThisFrame
  def touch = touchLocation.isDefined

  def addEvent(motionEvent: MotionEvent): Unit = {
    // Add the new event in a new thread so we don't freeze the main thread
    val thread = new Thread(new Runnable() {
      override def run(): Unit = {
        events.synchronized {
          events += motionEvent
        }
      }
    })
    thread.start()
  }

  private def processEvents(): Unit = {
    events.synchronized {
      for (event <- events) {
        processEvent(event)
      }
      events.clear()
    }
  }

  def onFrameChange(): Unit = {
    touchedThisFrame = false
    upThisFrame = false
    processEvents()
  }

  private def processEvent(event: MotionEvent): Unit = {
    event.getAction match {
      case MotionEvent.ACTION_UP => resetPointer()
      case MotionEvent.ACTION_CANCEL => resetPointer()
      case MotionEvent.ACTION_DOWN => {
        touchedThisFrame = true
        updatePointer(event)
      }
      case MotionEvent.ACTION_MOVE => updatePointer(event)
    }
    lastEventLoc = (event.getX, event.getY)
  }

  private def resetPointer(): Unit = {
    this.currentTouchLocation = None
    this.upThisFrame = true
  }

  private def updatePointer(event: MotionEvent): Unit = {
    this.currentTouchLocation = Some((event.getX, event.getY))
  }

  def isTouchInside(x: Float, y: Float, width: Float, height: Float): Boolean = {
    if (!touch) {
      false
    } else {
      touchX.get >= x && touchX.get <= x + width && touchY.get >= y && touchY.get <= y + height
    }
  }

}
