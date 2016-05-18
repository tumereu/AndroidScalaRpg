package com.tume.engine.gui

/**
  * Created by tume on 5/18/16.
  */
class UIInstantButton extends UIButton {

  override def onClick(): Unit = {}
  override def onTouch(): Unit = onPress()

}
