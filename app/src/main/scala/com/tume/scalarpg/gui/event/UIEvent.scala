package com.tume.scalarpg.gui.event

/**
  * Created by tume on 5/13/16.
  */
abstract class UIEvent {

  var view: Option[String] = None
  var id: Option[String] = None

}
case class ButtonEvent() extends UIEvent {}
