package com.tume.engine

import android.support.v4.view.MotionEventCompat
import android.util.Log
import android.view.MotionEvent
import com.tume.engine.model.{Shape, Vec2}
import com.tume.engine.util.Calc

/**
  * Created by tume on 5/12/16.
  */
object Input  {

  private var touchLocations = Vector[Touch]()
  private var oldTouchLocations = Vector[Touch]()

  private var holdPresses = Vector.empty[Touch]
  private var longPresses = Vector.empty[Touch]

  private var newPointers = Vector.empty[Touch]
  var removedPointers = Vector.empty[Touch]
  private var quickTaps = Vector.empty[Touch]
  private var taps = Vector.empty[Touch]
  var drags = Vector.empty[Drag]
  private var touches = Vector.empty[Touch]

  def pointers = touches.map(_.loc)

  /**
    * If a shape was quickly tapped this frame, that is a touch began and ended both inside that shape's borders
    * and happened within a certain time constrait, see Touch.isQuickTap
    */
  def quickTap(shape: Shape): Boolean = quickTaps.exists(p => shape.contains(p.startLoc.get) && shape.contains(p.loc))
  def quickTap: Boolean = quickTaps.nonEmpty

  /**
    * If a shape was tapped this frame, that is a touch began and ended both inside that shape's borders.
    */
  def tap(shape: Shape) : Boolean = taps.exists(p => shape.contains(p.startLoc.get) && shape.contains(p.loc))
  def tap : Boolean = taps.nonEmpty

  /**
    * Returns true if a new touch was started inside this shape this frame.
    */
  def touchStarted(shape: Shape) : Boolean = newPointers.exists(p => shape.contains(p.startLoc.get))
  def touchStarted : Boolean = newPointers.nonEmpty

  /**
    * Returns true if someone is constantly touching a shape. This requires that the touch both started
    * inside the shape and is currently inside the shape
    */
  def touching(shape: Shape) : Boolean = touchLocations.exists(p => shape.contains(p.startLoc.get) && shape.contains(p.loc))

  /**
    * A touch is hovering over a shape if it is currently contained in that shape. The touches starting
    * location has no effect on whether hovering is true or not.
    */
  def hovering(shape: Shape) : Boolean = touchLocations.exists(p => shape.contains(p.loc))

  /**
    * Return the smallest distance a quick tap that was performed this frame has to the given location
    * If no quick taps were performed, this returns None
    */
  def quickTapDistance(vec2: Vec2) : Option[Float] = distance(vec2, quickTaps.map(_.loc))
  def touchStartDistance(vec2: Vec2) : Option[Float] = distance(vec2, newPointers.map(_.loc))

  def touchesLifted = removedPointers.nonEmpty && touches.isEmpty
  def touchLiftLocations = removedPointers.map(_.loc)

  private def distance(vec2: Vec2, v: Vector[Vec2]): Option[Float] = {
    val o = v.sortBy(_.dist(vec2)).headOption
    if (o.isDefined) {
      Some(o.get.dist(vec2))
    } else {
      None
    }
  }

  def addEvent(motionEvent: MotionEvent): Unit = {
    touchLocations.synchronized {
      processEvent(motionEvent)
    }
  }

  def onFrameChange(): Unit = {
    touches = touchLocations.synchronized { touchLocations }
    holdPresses = Vector.empty[Touch]
    longPresses = Vector.empty[Touch]
    for (touch <- touches) {
      if (touch.isLongPress) {
        holdPresses = holdPresses :+ touch
        if (!touch.longPressTriggered) {
          touch.longPressTriggered = true
          longPresses = longPresses :+ touch
        }
      }
    }
    newPointers = newTouches
    removedPointers = removedTouches
    taps = removedPointers.filterNot(_.isDrag)
    quickTaps = taps.filter(_.isQuickTap)
    drags = Vector.empty[Drag]
    for (t <- touches) {
      if (t.isDrag) {
        drags = drags :+ new Drag(t.prevLoc.get, t.loc)
      }
    }

    oldTouchLocations = Vector.empty[Touch] ++ touches
  }

  private def processEvent(event: MotionEvent): Unit = {
    event.getActionMasked match {
      case MotionEvent.ACTION_UP => pointerRemoved(event)
      case MotionEvent.ACTION_POINTER_UP => pointerRemoved(event)
      case MotionEvent.ACTION_CANCEL => pointerRemoved(event)
      case MotionEvent.ACTION_DOWN => pointerAdded(event)
      case MotionEvent.ACTION_POINTER_DOWN => pointerAdded(event)
      case MotionEvent.ACTION_MOVE => pointerMoved(event)
    }
  }

  private def pointerAdded(event: MotionEvent): Unit = {
    val index = MotionEventCompat.getActionIndex(event)
    val t = new Touch(index)
    t update Vec2(event.getX, event.getY)
    touchLocations = touchLocations.filterNot(index == _.index) :+ t
  }

  private def pointerRemoved(event: MotionEvent): Unit = {
    val index = MotionEventCompat.getActionIndex(event)
    touchLocations = touchLocations.filterNot(index == _.index)
    touchLocations.foreach(t => if (t.index > index) t.index -= 1)
  }

  private def pointerMoved(event: MotionEvent) = {
    touchLocations = touchLocations.sortBy(_.index)
    for (i <- 0 until Calc.min(touchLocations.size, event.getPointerCount)) {
      touchLocations(i) update Vec2(event.getX(i), event.getY(i))
    }
  }

  private def newTouches : Vector[Touch] = {
    val b = Vector.newBuilder[Touch]
    for (t <- touchLocations) {
      if (!oldTouchLocations.map(_.identifier).contains(t.identifier)) {
        b += t
      }
    }
    b.result()
  }

  private def removedTouches : Vector[Touch] = {
    val b = Vector.newBuilder[Touch]
    for (t <- oldTouchLocations) {
      if (!touchLocations.map(_.identifier).contains(t.identifier)) {
        b += t
      }
    }
    b.result()
  }
}
class Touch(i: Int) {
  var startLoc = Option[Vec2](null)
  var prevLoc = Option[Vec2](null)
  var currentLoc = Option[Vec2](null)
  private var drag = false
  def loc = if (currentLoc.isEmpty) Vec2.zero else currentLoc.get

  def update(vec: Vec2): Unit = {
    if (startLoc.isEmpty) {
      startLoc = Some(vec)
    }
    prevLoc = currentLoc
    currentLoc = Some(vec)
    if (startLoc.get.dist(currentLoc.get) > 35f) {
      drag = true
    }
  }

  var index = i
  val identifier = java.util.UUID.randomUUID().toString
  val createTime = System.currentTimeMillis()

  var longPressTriggered = false

  def isQuickTap = System.currentTimeMillis() - createTime < 400
  def isLongPress = System.currentTimeMillis() - createTime > 1000
  def isDrag = drag
}
class Drag(val start: Vec2, val end: Vec2) {
  def len = start dist end
}
