package com.tume.scalarpg.model

import com.tume.engine.gui.model.UIModel
import com.tume.scalarpg.R

/**
  * Created by tume on 8/27/16.
  */
class Area extends UIModel {

  var name = "Area"
  var menuImage = R.drawable.desert_temple

  override def icon = Some(menuImage)
}
