package com.tume.engine.model

import android.graphics.Canvas
import com.tume.engine.Game
import com.tume.engine.model.Border.Border
import com.tume.engine.util.Calc

/**
  * Created by tume on 7/7/16.
  */
abstract class SGObject {

  var loc = Vec2(0f, 0f)
  var speed = Vec2(0f, 0f)

  var dampening = 1f

  var mass = 1f

  def collisionGroup: Int = -1
  def collidesWith = Seq.empty[Int]

  def boundingBox : Shape

  def update(delta: Float) = {
    loc += speed * delta
    speed *= Calc.pow(dampening, delta)
  }
  
  def applyForce(force: Vec2) = {
    this.speed += force / mass
  }

  def render(c: Canvas)
  def isRemovable: Boolean

  def onCollision(another: SGObject) = { }
  def onPhysicsCollision(another: SGObject) = {}
  def onBorderCollision(borders: Map[Border, Boolean], bounds: Rect) = { }
  def onBorderExit(borders: Map[Border, Boolean], bounds: Rect) = { }

  def bounceFrom(borders: Map[Border, Boolean], bounds: Rect): Unit = {
    if ((borders(Border.Left) && speed.x < 0) || (borders(Border.Right) && speed.x > 0)) {
      speed = speed.invertX
      if (borders(Border.Left)) loc = Vec2(bounds.left + boundingBox.width / 2, loc.y)
      if (borders(Border.Right)) loc = Vec2(bounds.right - boundingBox.width / 2, loc.y)
    }
    if ((borders(Border.Top) && speed.y < 0) || (borders(Border.Bottom) && speed.y > 0)) {
      speed = speed.invertY
      if (borders(Border.Top)) loc = Vec2(loc.x, bounds.top + boundingBox.height / 2)
      if (borders(Border.Bottom)) loc = Vec2(loc.x, bounds.bottom - boundingBox.height / 2)
    }
  }

  def stopTo(borders: Map[Border, Boolean], bounds: Rect): Unit = {
    if ((borders(Border.Left) && speed.x < 0) || (borders(Border.Right) && speed.x > 0)) {
      speed = Vec2(0, speed.y)
      if (borders(Border.Left)) loc = Vec2(bounds.left + boundingBox.width / 2, loc.y)
      if (borders(Border.Right)) loc = Vec2(bounds.right - boundingBox.width / 2, loc.y)
    }
    if ((borders(Border.Top) && speed.y < 0) || (borders(Border.Bottom) && speed.y > 0)) {
      speed = Vec2(speed.x, 0)
      if (borders(Border.Top)) loc = Vec2(loc.x, bounds.top + boundingBox.height / 2)
      if (borders(Border.Bottom)) loc = Vec2(loc.x, bounds.bottom - boundingBox.height / 2)
    }
  }

  def onAdd(game: Game) : Unit = { }

}
