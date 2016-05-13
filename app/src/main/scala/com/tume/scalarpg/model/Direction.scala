package com.tume.scalarpg.model

/**
  * Created by tume on 5/13/16.
  */
object Direction extends Enumeration {
  type Direction = Value
  val Left, Right, Up, Down = Value

  def toCoordinates(dir: Direction) : (Int, Int) = {
    dir match {
      case Left => (-1, 0)
      case Right => (1, 0)
      case Up => (0, -1)
      case Down => (0, 1)
    }
  }

  def atCoordinates(loc: (Int, Int), dir: Direction) = (loc._1 + toCoordinates(dir)._1, loc._2 + toCoordinates(dir)._2)
}
