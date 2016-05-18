package com.tume.engine.gui

/**
  * Created by tume on 5/12/16.
  */
trait UIView {

  def name: String
  def build : Seq[UIBuilder]

  def bottom = UIBuilder(new UISpace()).bottom()
  def top = UIBuilder(new UISpace()).top()
  def left = UIBuilder(new UISpace()).left()
  def right = UIBuilder(new UISpace()).right()
}
