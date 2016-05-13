package com.tume.scalarpg.ui

import com.tume.engine.gui.{UIProgressBar, UIBuilder, UIButton, UIView}
import com.tume.scalarpg.R

import scala.collection.mutable

/**
  * Created by tume on 5/12/16.
  */
class GameUI extends UIView {
  override def name: String = "GameUI"

  override def build: Seq[UIBuilder] = {
    var view = mutable.Buffer[UIBuilder]()
    // Movement buttons
    val moveRight = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_right).right().id("moveRight")
    val moveDown = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_down).bottom().leftOf(moveRight).id("moveDown")
    val moveLeft = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_left).leftOf(moveDown).id("moveLeft")
    view += moveDown
    view += moveRight.above(moveDown)
    view += moveLeft.above(moveDown)
    val moveUp = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_up).above(moveRight).leftOf(moveRight).id("moveUp")
    view += moveUp
    // Bars
    val timeBar = UIBuilder(new UIProgressBar()).absWidth(1f).height(0.05f).left().top().pad().color1(0xff00b7eb).color2(0xff00ffff).id("timeBar")
    view += timeBar
    val gameCanvas = UIBuilder(new GameCanvas()).absWidth(1f).absHeight(0.55f).left().below(timeBar).id("gameCanvas")
    view += gameCanvas
    val healthBar = UIBuilder(new UIProgressBar()).absWidth(0.7f).height(0.12f).left().below(gameCanvas).color1(0xff990808).color2(0xffff2222).id("healthBar")
    val manaBar = UIBuilder(new UIProgressBar()).absWidth(0.3f).height(0.12f).rightOf(healthBar).below(gameCanvas).color1(0xff080899).color2(0xff2222ff).id("manaBar")
    view += manaBar.pad()
    view += healthBar.pad()
    view
  }
}
