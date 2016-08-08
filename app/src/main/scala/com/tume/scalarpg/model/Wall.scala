package com.tume.scalarpg.model

import com.tume.engine.util.Bitmaps

/**
  * Created by tume on 5/13/16.
  */
class Wall(drawable: Int) extends TileObject {

  this.bitmap = Some(Bitmaps.get(drawable))

  override def relativeSize = 1f

  override def isPassable = false

}
