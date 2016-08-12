package com.tume.scalarpg

import android.graphics.{Canvas, Paint}
import android.util.Log
import com.tume.scalarpg.model.Direction._
import com.tume.engine.Game
import com.tume.engine.gui._
import com.tume.engine.gui.event.{ButtonEvent, UIEvent}
import com.tume.engine.util.{Rand, Calc, Bitmaps, DisplayUtils}
import com.tume.scalarpg.model._
import com.tume.scalarpg.model.item.EquipSlot
import com.tume.scalarpg.model.potion.{ExperiencePotion, ManaPotion, Potion, HealthPotion}
import com.tume.scalarpg.model.property.{Healing, Damage}
import com.tume.scalarpg.ui._
import com.tume.engine.effect._

import scala.util.Random

/**
  * Created by tume on 5/11/16.
  */
class TheGame extends Game {

  var currentTime = 0f
  var fullTurnsInRound = 10
  var currentRound = 1

  var floor = Map[(Int, Int), Tile]()
  var floorWidth, floorHeight = 0

  var player : Hero = null
  var enemies = Vector.empty[Enemy]

  var healthBar, manaBar, xpBar : UIProgressBar = null
  var healthPotion, manaPotion, xpPotion : UIButton = null

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
    spawnRoundEnemies()
  }

  def spawnRoundEnemies(): Unit = {
    var spawned = 1
    for (i <- 1 to 7 + currentRound * 3) {
      if (tryToSpawnEnemy(spawned)) {
        spawned += 1
      }
    }
  }

  def roundTime = player.speed * fullTurnsInRound
  def danger = currentRound * 2 + 5

  def tileAt(loc: (Int, Int)) : Option[Tile] = floor.get(loc)

  override def update(delta: Float): Unit = {
    super.update(delta)
    healthBar.updateProgress(player.health.toInt, player.maxHealth.toInt)
    manaBar.updateProgress(player.mana.toInt, player.maxMana.toInt)
    xpBar.updateProgress(player.xp, player.reqXp)

    healthPotion.cornerText = player.potionAmount(HealthPotion().getClass).toString
    healthPotion.enabled = player.potionAmount(HealthPotion().getClass) > 0
    manaPotion.cornerText = player.potionAmount(ManaPotion().getClass).toString
    manaPotion.enabled = player.potionAmount(ManaPotion().getClass) > 0
    xpPotion.cornerText = player.potionAmount(ExperiencePotion().getClass).toString
    xpPotion.enabled = player.potionAmount(ExperiencePotion().getClass) > 0

    player.update(delta)
  }

  def playerActionDone(time: Float): Unit = {
    this.currentTime += time
    for (tile <- floor.values) {
      for (o <- tile.objects) {
        o.turnEnded(this, time)
      }
    }
    if (currentTime >= roundTime) {
      currentRound += 1
      currentTime -= roundTime
      for (tile <- floor.values) {
        for (o <- tile.objects) {
          o.roundEnded(this)
        }
      }
      spawnRoundEnemies()
    }
    tryToSpawnPotion()
    findUIComponent[UIProgressBar]("timeBar").updateRawProgress(Some(currentTime / roundTime))
  }

  def tryToSpawnPotion(): Unit = {
    if (Math.random() < 0.02f) {
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

  def addHealingObject(healing: Healing, loc: Tile): Unit = {
    val effect = new FloatingTextObject(Colors.healPaint(healing), healing.cleanAmount, gameCanvas.coordinatesForLocation(loc),
      1f, 1f, DisplayUtils.scale * -90f)
    effectSystem.add(effect)
  }

  def creatureDied(c: Creature): Unit = {
    enemies = enemies.filterNot(_ == c)
    if (player != c) c.currentTile.get.removeObject(c)
    c match {
      case e: Enemy => player.gainXp(e.property.xp)
      case _ =>
    }
  }

  def removeObject(tileObject: TileObject): Unit = {
    tileObject.currentTile.foreach(_.removeObject(tileObject))
  }

  def spawn(tileObject: TileObject): Unit = {
    val frees = this.floor.values.filter(_.objects.isEmpty).toSeq
    if (frees.nonEmpty) {
      val loc = frees((Math.random * frees.size).toInt)
      loc.addObject(tileObject)
      tileObject match {
        case enemy: Enemy =>  {
          enemies = enemies :+ enemy
          enemy.spawn()
        }
        case p: Potion => p.spawn()
        case _ =>
      }
    }
  }

  def tryToSpawnEnemy(n: Int): Boolean = {
    if (Math.random() < 1f / n || this.enemies.isEmpty) {
      val d = this.danger * 10 / (9 + n)
      val dang = Math.min(Rand.i(this.danger * 7 / 10, d), Rand.i(this.danger * 7 / 10, d))
      spawn(new Enemy(this, Enemies.byDanger(dang)))
      true
    } else {
      false
    }
  }

  override def render(canvas: Canvas): Unit = {
    super.render(canvas)
  }

  override def views: Seq[UIView] = Vector(new HeroUI(), new GameUI())

  override def onUIEvent(event: UIEvent): Unit = {
    event match {
      case e: ButtonEvent => {
        e.id.get match {
          case "moveRight" => player.move(Right)
          case "moveLeft" => player.move(Left)
          case "moveUp" => player.move(Up)
          case "moveDown" => player.move(Down)
          case "healthPotion" => player.quaffPotion(new HealthPotion().getClass)
          case "manaPotion" => player.quaffPotion(new ManaPotion().getClass)
          case "xpPotion" => player.quaffPotion(new ExperiencePotion().getClass)
          case _ =>
        }
      }
    }
  }

  override def init(): Unit = {
    createFloor()
    this.player.createStartingEquipment()

    gameCanvas = findUIComponent[GameCanvas]("gameCanvas")
    healthBar = findUIComponent[UIProgressBar]("healthBar")
    manaBar = findUIComponent[UIProgressBar]("manaBar")
    xpBar = findUIComponent[UIProgressBar]("xpBar")
    healthPotion = findUIComponent[UIButton]("healthPotion")
    manaPotion = findUIComponent[UIButton]("manaPotion")
    xpPotion = findUIComponent[UIButton]("xpPotion")

    gameCanvas.game = Some(this)
    findUIComponent[UIProgressBar]("timeBar").updateRawProgress(Some(currentTime / roundTime))

    refreshHeroUI()
  }

  def refreshHeroUI(): Unit = {
    findUIComponent[UIButton]("armor_select").register(this.player.equipment(EquipSlot.Body))
    findUIComponent[UIButton]("boots_select").register(this.player.equipment(EquipSlot.Boots))
    findUIComponent[UIButton]("helmet_select").register(this.player.equipment(EquipSlot.Helmet))
    findUIComponent[UIButton]("main_weapon_select").register(this.player.equipment(EquipSlot.MainHand))
    findUIComponent[UIButton]("off_weapon_select").register(this.player.equipment(EquipSlot.OffHand))
    findUIComponent[UIButton]("trinket_select").register(this.player.equipment(EquipSlot.Trinket))

    findUIComponent[UILabel]("info_health").text = Calc.clean(this.player.maxHealth, 0)
    findUIComponent[UILabel]("info_mana").text = Calc.clean(this.player.maxMana, 0)
  }
}
