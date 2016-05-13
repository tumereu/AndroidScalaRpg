package com.tume.scalarpg.model

import com.tume.scalarpg.R
import com.tume.scalarpg.gui.{UIButton, UIBuilder, UIView}

import scala.collection.mutable

/**
  * Created by tume on 5/12/16.
  */
class GameUI extends UIView {
  override def name: String = "GameUI"

  override def build: Seq[UIBuilder] = {
    var view = mutable.Buffer[UIBuilder]()
    var moveRight = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_right).right(0.0f).id("moveRight")
    val moveDown = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_down).bottom().leftOf(moveRight).id("moveDown")
    var moveLeft = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_left).leftOf(moveDown).id("moveLeft")
    moveRight = moveRight.above(moveDown)
    moveLeft = moveLeft.above(moveDown)
    val moveUp = UIBuilder(new UIButton).size(0.15f).drawable(R.mipmap.ic_arrow_up).above(moveRight).leftOf(moveRight).id("moveUp")
    view += moveRight
    view += moveLeft
    view += moveUp
    view += moveDown
    view
  }
}
