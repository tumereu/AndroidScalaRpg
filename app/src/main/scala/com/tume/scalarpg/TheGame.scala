package com.tume.scalarpg

import android.graphics.{Canvas, Paint}
import android.util.Log
import com.tume.engine.effect.FadingTextObject
import com.tume.scalarpg.model.Direction._
import com.tume.engine.Game
import com.tume.engine.gui.{UITheme, UIProgressBar, UIView}
import com.tume.engine.gui.event.{ButtonEvent, UIEvent}
import com.tume.engine.util.{Bitmaps, DisplayUtils}
import com.tume.scalarpg.model._
import com.tume.scalarpg.model.potion.{Potion, HealthPotion}
import com.tume.scalarpg.model.property.Damage
import com.tume.scalarpg.ui.{Colors, GameCanvas, Drawables, GameUI}
import com.tume.engine.effect._

/**
  * Created by tume on 5/11/16.
  */
class TheGame extends Game {

  var currentTime = 0f

  var floor = Map[(Int, Int), Tile]()
  var floorWidth, floorHeight = 0

  var player : Hero = null
  var enemies = Vector.empty[Enemy]

  var healthBar, manaBar, xpBar : UIProgressBar = null
  var gameCanvas : GameCanvas = null

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
      spawn(new Enemy(this))
    }
  }

  def tileAt(loc: (Int, Int)) : Option[Tile] = floor.get(loc)

  def update(delta: Double): Unit = {
    healthBar.updateProgress(player.health.toInt, player.maxHealth.toInt)
    manaBar.updateProgress(player.mana.toInt, player.maxMana.toInt)
    xpBar.updateProgress(player.xp, player.reqXp)

    player.update(delta)
  }

  def playerActionDone(time: Float): Unit = {
    this.currentTime += time
    for (tile <- floor.values) {
      for (o <- tile.objects) {
        o.turnEnded(this)
      }
    }
    if (currentTime >= player.speed) {
      currentTime -= player.speed
      for (tile <- floor.values) {
        for (o <- tile.objects) {
          o.roundEnded(this)
        }
      }
      tryToSpawnEnemy()
      tryToSpawnPotion()
    }
    findUIComponent("timeBar").get.asInstanceOf[UIProgressBar].updateRawProgress(Some(currentTime / player.speed))
  }

  def tryToSpawnPotion(): Unit = {
    if (Math.random() < 0.05f) {
      spawn(Potion.randomPotion)
    }
  }

  def addEnemyToPlayerDamageObject(damage: Damage, loc: Tile): Unit = {
    val target = (healthBar.x.toFloat + healthBar.width / 2, healthBar.y.toFloat + healthBar.height / 2)
    val start = gameCanvas.coordinatesForLocation(loc)
    val effect = new HomingTextObject(Colors.dmgPaint(damage), damage.cleanAmount, start,
      target, 45f * DisplayUtils.scale, (0.7f + Math.random() * 0.3f).toFloat)
    effect.onRemove = () => {
      healthBar.tick()
      player.takeDamage(damage)
    }
    effectSystem.add(effect)
  }

  def addPlayerToEnemyDamageObject(damage: Damage, loc: Tile): Unit = {
    val effect = new FloatingTextObject(Colors.dmgPaint(damage), damage.cleanAmount, gameCanvas.coordinatesForLocation(loc),
      1f, 1f, DisplayUtils.scale * -90f)
    effectSystem.add(effect)
  }

  def creatureDied(c: Creature): Unit = {
    enemies = enemies.filterNot(_ == c)
    if (player != c) {
      c.currentTile.get.removeObject(c)
    }
    if (c.isInstanceOf[Enemy]) {
      val e = c.asInstanceOf[Enemy]
      player.gainXp(e.xp)
    }
  }

  def removeObject(tileObject: TileObject): Unit = {
    tileObject.currentTile.foreach(_.removeObject(tileObject))
  }

  def spawn(tileObject: TileObject): Unit = {
    val frees = this.floor.values.filter(_.objects.isEmpty).toSeq
    if (!frees.isEmpty) {
      val loc = frees((Math.random * frees.size).toInt)
      loc.addObject(tileObject)
      if (tileObject.isInstanceOf[Enemy]) {
        enemies = enemies :+ tileObject.asInstanceOf[Enemy]
      }
    }
  }

  def tryToSpawnEnemy(): Unit = {
    if (Math.random() < 0.2 || this.enemies.isEmpty) {
      spawn(new Enemy(this))
    }
  }

  def render(canvas: Canvas): Unit = {
    canvas.drawColor(0xff000000)
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
          case _ =>
        }
      }
    }
  }

  override def init(): Unit = {
    createFloor()
    gameCanvas = findUIComponent("gameCanvas").get.asInstanceOf[GameCanvas]
    healthBar = findUIComponent("healthBar").get.asInstanceOf[UIProgressBar]
    manaBar = findUIComponent("manaBar").get.asInstanceOf[UIProgressBar]
    xpBar = findUIComponent("xpBar").get.asInstanceOf[UIProgressBar]

    gameCanvas.game = Some(this)
  }
}
