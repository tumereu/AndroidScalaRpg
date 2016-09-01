package com.tume.scalarpg

import android.graphics.{Canvas, Paint}
import android.util.Log
import com.tume.engine.gui.model.UIModel
import com.tume.engine.model.Vec2
import com.tume.scalarpg.GameState._
import com.tume.scalarpg.model.Direction._
import com.tume.engine.Game
import com.tume.engine.gui._
import com.tume.engine.gui.event.{ButtonEvent, UIEvent}
import com.tume.engine.util.{Rand, Calc, Bitmaps, DisplayUtils}
import com.tume.scalarpg.model._
import com.tume.scalarpg.model.hero.HeroClasses
import com.tume.scalarpg.model.item._
import com.tume.scalarpg.model.potion.{ExperiencePotion, ManaPotion, Potion, HealthPotion}
import com.tume.scalarpg.model.property.{Elements, Healing, Damage}
import com.tume.scalarpg.ui._
import com.tume.engine.effect._

/**
  * Created by tume on 5/11/16.
  */
class TheGame extends Game {

  var currentTime = 0f
  var fullTurnsInRound = 10
  var currentRound = 1

  var floor = Map[(Int, Int), Tile]()
  def floorWidth = selectedArea.width
  def floorHeight = selectedArea.height

  var inventory = Vector.empty[Equipment]

  var player : Hero = null
  var enemies = Vector.empty[Enemy]

  var healthBar, manaBar, xpBar : UIProgressBar = null
  var healthPotion, manaPotion, xpPotion : UIButton = null

  var lootGiven = 1

  var state: GameState = AdventureSelection
  var selectMainSlot = false

  var gameCanvas : GameCanvas = null

  var selectedAreaIndex = 0
  def selectedArea = Areas.areas(selectedAreaIndex % Areas.areas.length)

  def createFloor(): Unit = {
    this.currentTime = 0f
    this.currentRound = 1
    this.enemies = Vector.empty
    floor = Map[(Int, Int), Tile]()
    for (x <- 0 until floorWidth; y <- 0 until floorHeight) {
      floor += (x, y) -> new Tile(x, y, Rand.from(selectedArea.floorDrawables))
    }
    floor((3,4)).addObject(player)
    for (i <- 0 until Rand.i(selectedArea.wallAmount._1 , selectedArea.wallAmount._2)) {
      var t = Option[Tile](null)
      while (t.isEmpty) {
        t = floor.lift(Rand.i(floorWidth), Rand.i(floorHeight))
        if (t.get.objects.nonEmpty) {
          t = None
        }
      }
      t.get.addObject(new Wall(Rand.from(selectedArea.wallDrawables)))
    }
    spawnRoundEnemies()
  }

  def spawnRoundEnemies(): Unit = {
    var spawned = 1
    for (i <- 1 to 7 + currentRound) {
      if (tryToSpawnEnemy(spawned)) {
        spawned += 1
      }
    }
  }

  def roundTime = fullTurnsInRound
  def danger = currentRound * 2 + 5

  def tileAt(loc: (Int, Int)) : Option[Tile] = floor.get(loc)

  def addItem(equipment: Equipment): Unit = {
    inventory = (inventory :+ equipment).sortBy(a => -(a.rarity.affixes + a.rarity.resistances) * 1000 + a.itemLevel)
  }

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
    val target = Vec2(healthBar.x.toFloat + healthBar.width / 2, healthBar.y.toFloat + healthBar.height / 2)
    val start = gameCanvas.coordinatesForLocation(loc)
    val effect = new HomingTextObject(Colors.dmgPaint(damage.crit), damage.cleanAmount, start,
      target, 45f * DisplayUtils.scale, (0.7f + Math.random() * 0.3f).toFloat)
    effect.onRemove = () => {
      healthBar.tick()
      player.takeDamage(damage)
    }
    effectSystem.add(effect)
  }

  def addPlayerToEnemyDamageObject(damage: Damage, loc: Tile, miss: Boolean): Unit = {
    val text = if (miss) "Miss" else damage.cleanAmount
    val effect = new FloatingTextObject(Colors.dmgPaint(damage), text, gameCanvas.coordinatesForLocation(loc),
      1f, 1f, DisplayUtils.scale * -90f)
    effectSystem.add(effect)
  }

  def addHealingObject(healing: Healing, loc: Tile): Unit = {
    val effect = new FloatingTextObject(Colors.healPaint(healing), healing.cleanAmount, gameCanvas.coordinatesForLocation(loc),
      1f, 1f, DisplayUtils.scale * -90f)
    effectSystem.add(effect)
  }

  def rollLoot(enemy: Enemy): Unit = {
    if (enemy.elite) {
      if (Rand.f < Calc.max(0.5f, 5f / lootGiven)) {
        lootGiven += 1
        var eq = Equipment.generateNew(selectedArea.itemLevel + enemy.property.difficulty / 20, this)
        if (lootGiven == 2) {
          while (!eq.isInstanceOf[Weapon] || eq.asInstanceOf[Weapon].category != WeaponCategory.Sword) {
            eq = Equipment.generateNew(selectedArea.itemLevel + enemy.property.difficulty / 20, this)
          }
        }
        effectSystem.add(new FloatingTextObject(Colors.rarityPaint(eq.rarity), eq.name, gameCanvas.coordinatesForLocation(player.currentTile.get),
          1f, 1f, DisplayUtils.scale * -90f))
        addItem(eq)
      }
    }
  }

  def playerDied(): Unit = {
    changeState(GameState.AdventureSelection)
  }

  def creatureDied(c: Creature): Unit = {
    enemies = enemies.filterNot(_ == c)
    if (player != c) c.currentTile.get.removeObject(c)
    else playerDied()
    c match {
      case e: Enemy => {
        player.gainXp(e.property.xp)
        rollLoot(e)
      }
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
      spawn(new Enemy(this, selectedArea.byDanger(dang)))
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
    super.onUIEvent(event)
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
          case "select_prev_stage" => {
            this.selectedAreaIndex -= 1
            if (selectedAreaIndex < 0) selectedAreaIndex = Areas.areas.size - 1
            refreshHeroUI()
            }
          case "select_next_stage" => { this.selectedAreaIndex += 1; refreshHeroUI() }
          case "helmet_select" => showSelectionDialog("item_select", inventory.collect{case h: Helmet => h})
          case "armor_select" => showSelectionDialog("item_select", inventory.collect{case h: BodyArmor => h})
          case "boots_select" => showSelectionDialog("item_select", inventory.collect{case h: Boots => h})
          case "trinket_select" => showSelectionDialog("item_select", inventory.collect{case h: Trinket => h})
          case "main_weapon_select" => {
            selectMainSlot = true
            showSelectionDialog("item_select", inventory.collect{case h: Weapon => h}.filter(p => {
              player.heroClass.mainHandWeapons.contains(p.category) && player.equipment(EquipSlot.OffHand) != p
            }))
          }
          case "off_weapon_select" => {
            selectMainSlot = false
            showSelectionDialog("item_select", inventory.collect{case h: Weapon => h}.filter(p => {
              player.heroClass.offHandWeapons.contains(p.category) && player.equipment(EquipSlot.MainHand) != p
            }))
          }
          case "start_level" => {
            createFloor()
            changeState(GameState.Adventuring)
          }
          case _ =>
        }
      }
    }
  }

  def changeState(state: GameState): Unit = {
    this.state = state
    state match {
      case AdventureSelection => {
        player = new Hero(this, player.heroClass)
        uiSystem.show("HeroUI")
      }
      case Adventuring => {
        player.reset()
        uiSystem.show("GameUI")
      }
    }
  }

  override def onSelection(id: String, uIModel: UIModel, index: Int): Boolean = {
    id match {
      case "item_select" if state == AdventureSelection => {
        uIModel match {
          case e: Equipment => this.player.equipItem(e, this.selectMainSlot); refreshHeroUI()
          case _ =>
        }
      }
    }
    true
  }

  override def init(): Unit = {
    player = new Hero(this, Rand.from(HeroClasses.list))

    createFloor()

    gameCanvas = findUIComponent[GameCanvas]("gameCanvas")
    healthBar = findUIComponent[UIProgressBar]("healthBar")
    manaBar = findUIComponent[UIProgressBar]("manaBar")
    xpBar = findUIComponent[UIProgressBar]("xpBar")
    healthPotion = findUIComponent[UIButton]("healthPotion")
    manaPotion = findUIComponent[UIButton]("manaPotion")
    xpPotion = findUIComponent[UIButton]("xpPotion")

    gameCanvas.game = Some(this)
    findUIComponent[UIProgressBar]("timeBar").updateRawProgress(Some(currentTime / roundTime))
    findUIComponent[UIPanel]("stage_select_panel").register(this.selectedArea)

    findUIComponent[UIRadioButton]("difficulty_normal").toggle()

    player.calculateEquipmentStats()

    refreshHeroUI()
  }

  def refreshHeroUI(): Unit = {
    import Calc._
    import Elements._

    findUIComponent[UIButton]("armor_select").register(this.player.equipment(EquipSlot.Body))
    findUIComponent[UIButton]("boots_select").register(this.player.equipment(EquipSlot.Boots))
    findUIComponent[UIButton]("helmet_select").register(this.player.equipment(EquipSlot.Helmet))
    findUIComponent[UIButton]("main_weapon_select").register(this.player.equipment(EquipSlot.MainHand))
    findUIComponent[UIButton]("off_weapon_select").register(this.player.equipment(EquipSlot.OffHand))
    findUIComponent[UIButton]("trinket_select").register(this.player.equipment(EquipSlot.Trinket))

    findUIComponent[UILabel]("class_name").text = player.heroClass.name

    findUIComponent[UILabel]("info_health").text = clean(this.player.maxHealth, 0)
    findUIComponent[UILabel]("info_mana").text = clean(this.player.maxMana, 0)
    findUIComponent[UILabel]("info_attack_speed").text = clean(this.player.attackSpeed, 1)
    findUIComponent[UILabel]("info_speed").text = clean(this.player.movementSpeed, 1)
    findUIComponent[UILabel]("info_ability_power").text = clean(this.player.abilityPower, 0)
    findUIComponent[UILabel]("info_damage").text = player.attacks.map(a => clean(a.minDamage, 0) + "-" + clean(a.maxDamage, 0)).mkString("/")
    findUIComponent[UILabel]("info_crit_chance").text = player.attacks.map(a => clean(a.critChance * 100, 1)).mkString("/") + "%"
    findUIComponent[UILabel]("info_accuracy").text = player.attacks.map(a => clean(a.accuracy * 100, 1)).mkString("/") + "%"

    findUIComponent[UILabel]("res_physical").text = this.player.resistances(Physical) + "%"
    findUIComponent[UILabel]("res_fire").text = this.player.resistances(Fire) + "%"
    findUIComponent[UILabel]("res_frost").text = this.player.resistances(Frost) + "%"
    findUIComponent[UILabel]("res_lightning").text = this.player.resistances(Electric) + "%"
    findUIComponent[UILabel]("res_holy").text = this.player.resistances(Holy) + "%"
    findUIComponent[UILabel]("res_dark").text = this.player.resistances(Dark) + "%"
    findUIComponent[UILabel]("res_poison").text = this.player.resistances(Poison) + "%"
    findUIComponent[UILabel]("res_arcane").text = this.player.resistances(Arcane) + "%"

    findUIComponent[UIPanel]("stage_select_panel").register(selectedArea)
    findUIComponent[UILabel]("stage_name").text = selectedArea.name
    for (i <- 0 to 5) {
      findUIComponent[UILabel]("info" + i).text = selectedArea.description.lift(i).getOrElse("")
    }
  }
}
