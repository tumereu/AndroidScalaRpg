package com.tume.engine.gui

/**
  * Created by tume on 5/18/16.
  */
class UIPanelToggleButton extends UIButton {

  var panel: Option[UIPopupPanel] = None

  override def onPress() : Unit = {
    if (panel.isDefined && uiSystem.activePopups.contains(panel.get)) {
      panel.foreach(uiSystem.removePopup(_))
    } else {
      panel.foreach(uiSystem.addPopup(_))
    }
  }

}
