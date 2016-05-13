package com.tume.scalarpg

import android.graphics.{Canvas, Paint}
import android.util.Log
import com.tume.scalarpg.model.Direction._
import com.tume.engine.Game
import com.tume.engine.gui.{UIProgressBar, UIView}
import com.tume.engine.gui.event.{ButtonEvent, UIEvent}
import com.tume.engine.util.{Bitmaps, DisplayUtils}
import com.tume.scalarpg.model._
import com.tume.scalarpg.ui.{GameCanvas, Drawables, GameUI}

/**
  * Created by tume on 5/11/16.
  */
class TheGame extends Game {

  var currentTime = 0f

  var floor = Map[(Int, Int), Tile]()
  var floorWidth, floorHeight = 0

  var player : Hero = null
  var enemies = Vector.empty[Enemy]

  var healthBar, manaBar : UIProgressBar = null

  def createFloor(): Unit = {
    floor = Map[(Int, Int), Tile]()
    floorWidth = 6
    floorHeight = 6
    for (x <- 0 until floorWidth; y <- 0 until floorHeight) {
      floor += (x, y) -> new Tile(x, y, Drawables.random(Drawables.floorsSandStone))
    }
    player = new Hero(this)
    floor((3,4)).addObject(player)
    floor((1, 1)).addObject(new Wall(Drawables.random(Drawables.wallsStoneBrown)))
    floor((2, 1)).addObject(new Wall(Drawables.random(Drawables.wallsStoneBrown)))
    floor((1, 2)).addObject(new Wall(Drawables.random(Drawables.wallsStoneBrown)))
    for (i <- 1 to 3) {
      spawnEnemy()
    }
  }

  def tileAt(loc: (Int, Int)) : Option[Tile] = floor.get(loc)

  def update(delta: Double): Unit = {
    healthBar.updateProgress(player.renderedHealth / player.maxHealth)
    manaBar.updateProgress(player.renderedMana / player.maxMana)
  }

  def playerActionDone(time: Float): Unit = {
    this.currentTime += time
    if (currentTime >= player.speed) {
      currentTime -= player.speed
      enemyTurn()
    }
    this.findUIComponent("timeBar").get.asInstanceOf[UIProgressBar].updateProgress(currentTime / player.speed)
  }

  def enemyTurn(): Unit = {
    for (e <- enemies) {
      e.attackCreature(player)
    }
    this.tryToSpawnEnemy()
  }

  def spawnEnemy(): Unit = {
    val frees = this.floor.values.filter(_.objects.isEmpty).toSeq
    if (!frees.isEmpty) {
      val loc = frees((Math.random * frees.size).toInt)
      val enemy = new Enemy(this)
      loc.addObject(enemy)
      enemies = enemies :+ enemy
    }
  }

  def tryToSpawnEnemy(): Unit = {
    if (Math.random() < 0.2) {
      spawnEnemy()
    }
  }

  def render(canvas: Canvas): Unit = {
  }

  override def views: Seq[UIView] = Vector(new GameUI())

  override def onUIEvent(event: UIEvent): Unit = {
    event match {
      case e: ButtonEvent => {
        e.id.get match {
          case "moveRight" => player.move(Right)
          case "moveLeft" => player.move(Left)
          case "moveUp" => player.move(Up)
          case "moveDown" => player.move(Down)
        }
      }
    }
  }

  override def init(): Unit = {
    createFloor()
    findUIComponent("gameCanvas").get.asInstanceOf[GameCanvas].game = Some(this)
    healthBar = findUIComponent("healthBar").get.asInstanceOf[UIProgressBar]
    manaBar = findUIComponent("manaBar").get.asInstanceOf[UIProgressBar]
  }
}
