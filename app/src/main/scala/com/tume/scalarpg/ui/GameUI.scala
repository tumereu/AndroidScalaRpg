package com.tume.scalarpg.ui

import android.util.Log
import com.tume.engine.gui._
import com.tume.engine.util.DisplayUtils
import com.tume.scalarpg.R

import scala.collection.mutable

/**
  * Created by tume on 5/12/16.
  */
class GameUI extends UIView {
  override def name: String = "GameUI"

  override def build: Seq[UIBuilder] = {
    var view = mutable.Buffer[UIBuilder]()
    // Bars
    val timeBar = UIBuilder(new UIProgressBar()).absWidth(1f).height(0.05f).left().top().pad().color1(0xff00b7eb).color2(0xff00ffff).id("timeBar")
    val gameCanvas = UIBuilder(new GameCanvas()).absWidth(1f).absHeight(0.55f).left().below(timeBar).id("gameCanvas")
    view += gameCanvas
    view += timeBar.pad()
    val healthBar = UIBuilder(new UIProgressBar()).absWidth(0.7f).height(0.12f).left().below(gameCanvas).color1(0xff089908).color2(0xff22ff22).id("healthBar")
    val manaBar = UIBuilder(new UIProgressBar()).absWidth(0.3f).height(0.12f).rightOf(healthBar).below(gameCanvas).color1(0xff080899).color2(0xff2222ff).id("manaBar")
    view += manaBar.pad(DisplayUtils.scale.toInt)
    view += healthBar.color3(0xff990808).color4(0xffff2222).pad(DisplayUtils.scale.toInt)
    val xpBar = UIBuilder(new UIProgressBar()).absWidth(1f).height(0.035f).left().below(healthBar).color1(0xff993399).color2(0xffcc12cc).id("xpBar")
    view += xpBar.pad()
    // Ability and potion buttons
    val potionButton = UIBuilder(new UIPanelToggleButton()).size(0.175f).drawable(R.drawable.potions).left(0.05f).below(xpBar).id("potions")
    val ability0Button = UIBuilder(new UIButton()).size(0.175f).rightOf(potionButton).alignTop(potionButton).id("ability0")
    val ability1Button = UIBuilder(new UIButton()).size(0.175f).below(potionButton).alignLeft(potionButton).id("ability1")
    val ability2Button = UIBuilder(new UIButton()).size(0.175f).rightOf(ability1Button).alignTop(ability1Button).id("ability2")
    val ability3Button = UIBuilder(new UIButton()).size(0.175f).below(ability1Button).alignLeft(ability1Button).id("ability3")
    val ability4Button = UIBuilder(new UIButton()).size(0.175f).rightOf(ability3Button).alignTop(ability3Button).id("ability4")
    view += potionButton.pad(DisplayUtils.scale.toInt)
    view += ability0Button.pad(DisplayUtils.scale.toInt)
    view += ability1Button.pad(DisplayUtils.scale.toInt)
    view += ability2Button.pad(DisplayUtils.scale.toInt)
    view += ability3Button.pad(DisplayUtils.scale.toInt)
    view += ability4Button.pad(DisplayUtils.scale.toInt)

    // Movement buttons
    val middlePoint = UIBuilder(new UISpace()).xBetween(ability0Button, right).yBetween(xpBar, bottom)
    val moveBtnSize = 0.15f
    val moveBtnSpace = moveBtnSize / 2

    view += UIBuilder(new UIInstantButton).size(moveBtnSize).drawable(R.mipmap.ic_arrow_right).alignCenter(middlePoint).rightOf(middlePoint, moveBtnSpace).id("moveRight")
    view += UIBuilder(new UIInstantButton).size(moveBtnSize).drawable(R.mipmap.ic_arrow_left).alignCenter(middlePoint).leftOf(middlePoint, moveBtnSpace).id("moveLeft")
    view += UIBuilder(new UIInstantButton).size(moveBtnSize).drawable(R.mipmap.ic_arrow_up).alignCenter(middlePoint).above(middlePoint, moveBtnSpace).id("moveUp")
    view += UIBuilder(new UIInstantButton).size(moveBtnSize).drawable(R.mipmap.ic_arrow_down).alignCenter(middlePoint).below(middlePoint, moveBtnSpace).id("moveDown")

    val potionPanel = new UIPopupPanel()
    val hpPotion = UIBuilder(new UIButton).size(0.15f).drawable(R.drawable.potion_ruby).left(0.05f).alignBottom(healthBar).id("healthPotion")
    val manaPotion = UIBuilder(new UIButton).size(0.15f).drawable(R.drawable.potion_brilliant_blue).rightOf(hpPotion, 0.01f).alignBottom(healthBar).id("manaPotion")
    val xpPotion = UIBuilder(new UIButton).size(0.15f).drawable(R.drawable.potion_emerald).rightOf(manaPotion, 0.01f).alignBottom(healthBar).id("xpPotion")
    potionPanel += hpPotion.resolve
    potionPanel += manaPotion.resolve
    potionPanel += xpPotion.resolve

    potionButton.uiComponent.asInstanceOf[UIPanelToggleButton].panel = Some(potionPanel)

    view += UIBuilder(potionPanel)

    view
  }
}
