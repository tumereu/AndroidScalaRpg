package com.tume.scalarpg.ui

import com.tume.engine.gui._
import com.tume.engine.gui.builder.UIBuilder
import com.tume.engine.util.DisplayUtils
import com.tume.scalarpg.R

import scala.collection.mutable

/**
  * Created by tume on 5/12/16.
  */
class GameUI extends UIView {

  override def build: Seq[UIBuilder[_ <: UIComponent]] = {
    var view = mutable.Buffer[UIBuilder[_ <: UIComponent]]()
    // Bars
    val timeBar = UIBuilder.progressBar.main(0xff00b7eb, 0xff00ffff).absWidth(1f).height(0.05f).left().top().pad().id("timeBar")
    val gameCanvas = UIBuilder[GameCanvas](new GameCanvas()).absWidth(1f).absHeight(0.55f).left().below(timeBar).id("gameCanvas")
    view += gameCanvas
    view += timeBar.pad()
    val healthBar = UIBuilder.progressBar.main(0xff089908, 0xff22ff22).tick(0xff990808, 0xffff2222).absWidth(0.7f).height(0.12f).left().below(gameCanvas)
    val manaBar = UIBuilder.progressBar.main(0xff080899, 0xff2222ff).absWidth(0.3f).height(0.12f).rightOf(healthBar).below(gameCanvas).id("manaBar")
    view += manaBar.pad(DisplayUtils.scale.toInt)
    view += healthBar.pad(DisplayUtils.scale.toInt).id("healthBar")
    val xpBar = UIBuilder.progressBar.main(0xff993399, 0xffcc12cc).absWidth(1f).height(0.035f).left().below(healthBar).id("xpBar")
    view += xpBar.pad()
    // Ability and potion buttons
    val potionButton = UIBuilder.panelButton.img(R.drawable.potions).size(0.175f).left(0.05f).below(xpBar).id("potions")
    val ability0Button = UIBuilder.button.size(0.175f).rightOf(potionButton).alignTop(potionButton).id("ability0")
    val ability1Button = UIBuilder.button.size(0.175f).below(potionButton).alignLeft(potionButton).id("ability1")
    val ability2Button = UIBuilder.button.size(0.175f).rightOf(ability1Button).alignTop(ability1Button).id("ability2")
    val ability3Button = UIBuilder.button.size(0.175f).below(ability1Button).alignLeft(ability1Button).id("ability3")
    val ability4Button = UIBuilder.button.size(0.175f).rightOf(ability3Button).alignTop(ability3Button).id("ability4")
    view += potionButton.pad(DisplayUtils.scale.toInt)
    view += ability0Button.pad(DisplayUtils.scale.toInt)
    view += ability1Button.pad(DisplayUtils.scale.toInt)
    view += ability2Button.pad(DisplayUtils.scale.toInt)
    view += ability3Button.pad(DisplayUtils.scale.toInt)
    view += ability4Button.pad(DisplayUtils.scale.toInt)

    // Movement buttons
    val middlePoint = UIBuilder.space.xBetween(ability0Button, right).yBetween(xpBar, bottom)
    val moveBtnSize = 0.15f
    val moveBtnSpace = moveBtnSize / 2

    view += UIBuilder.instantButton.img(R.mipmap.ic_arrow_right).size(moveBtnSize).alignCenter(middlePoint).rightOf(middlePoint, moveBtnSpace).id("moveRight")
    view += UIBuilder.instantButton.img(R.mipmap.ic_arrow_left).size(moveBtnSize).alignCenter(middlePoint).leftOf(middlePoint, moveBtnSpace).id("moveLeft")
    view += UIBuilder.instantButton.img(R.mipmap.ic_arrow_up).size(moveBtnSize).alignCenter(middlePoint).above(middlePoint, moveBtnSpace).id("moveUp")
    view += UIBuilder.instantButton.img(R.mipmap.ic_arrow_down).size(moveBtnSize).alignCenter(middlePoint).below(middlePoint, moveBtnSpace).id("moveDown")

    val potionPanel = new UIPopupPanel()
    val hpPotion = UIBuilder.button.size(0.15f).img(R.drawable.potion_ruby).left(0.05f).alignBottom(healthBar).id("healthPotion")
    val manaPotion = UIBuilder.button.size(0.15f).img(R.drawable.potion_brilliant_blue).rightOf(hpPotion, 0.01f).alignBottom(healthBar).id("manaPotion")
    val xpPotion = UIBuilder.button.size(0.15f).img(R.drawable.potion_emerald).rightOf(manaPotion, 0.01f).alignBottom(healthBar).id("xpPotion")
    potionPanel += hpPotion.resolve
    potionPanel += manaPotion.resolve
    potionPanel += xpPotion.resolve

    potionButton.uiComponent.asInstanceOf[UIPanelToggleButton].panel = Some(potionPanel)

    view += UIBuilder(potionPanel)

    view
  }
}
