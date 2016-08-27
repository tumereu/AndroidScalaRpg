package com.tume.scalarpg.model.property

/**
  * Created by tume on 5/13/16.
  */
object Elements extends Enumeration {
  type Element = Value
  val Physical, Fire, Electric, Frost, Dark, Holy, Poison, Arcane = Value
  val all = Vector(Physical,Fire,Frost,Electric,Dark,Holy,Poison,Arcane)
}
