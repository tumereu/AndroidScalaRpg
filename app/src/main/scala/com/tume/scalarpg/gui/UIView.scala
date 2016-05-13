package com.tume.scalarpg.gui

/**
  * Created by tume on 5/12/16.
  */
trait UIView {

  def name: String
  def build : Seq[UIBuilder]
}
