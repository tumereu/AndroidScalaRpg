package com.tume.engine.gui

import com.tume.engine.util.DisplayUtils

/**
  * Created by tume on 5/12/16.
  */
class UIBuilder(val uiComponent: UIComponent) {

  private var padding = UIBuilderSettings.padding

  private def toPixels(float: Float) = (float * DisplayUtils.screenSize).toInt

  def width(float: Float) : UIBuilder = {
    uiComponent.width = toPixels(float)
    this
  }

  def height(float: Float) : UIBuilder = {
    uiComponent.height = toPixels(float)
    this
  }

  def absHeight(float: Float) : UIBuilder = {
    uiComponent.height = (float * DisplayUtils.screenHeight).toInt
    this
  }

  def absWidth(float: Float) : UIBuilder = {
    uiComponent.width = (float * DisplayUtils.screenWidth).toInt
    this
  }

  def padding(int: Int) : UIBuilder = {
    padding = int
    this
  }

  def size(float: Float) : UIBuilder = width(float).height(float)

  def x(float: Float) : UIBuilder = {
    uiComponent.x = toPixels(float)
    this
  }

  def y(float: Float) : UIBuilder = {
    uiComponent.y = toPixels(float)
    this
  }

  def ax(float: Float) : UIBuilder = {
    uiComponent.x = (float * DisplayUtils.screenWidth).toInt
    this
  }

  def ay(float: Float) : UIBuilder = {
    uiComponent.y = (float * DisplayUtils.screenHeight).toInt
    this
  }

  def top(float: Float = 0) : UIBuilder = {
    uiComponent.y = padding + toPixels(float)
    this
  }

  def bottom(float: Float = 0) : UIBuilder = {
    uiComponent.y = DisplayUtils.screenHeight - uiComponent.height - padding - toPixels(float)
    this
  }

  def left(float: Float = 0) : UIBuilder = {
    uiComponent.x = padding + toPixels(float)
    this
  }

  def right(float: Float = 0) : UIBuilder = {
    uiComponent.x = DisplayUtils.screenWidth - uiComponent.width - padding - toPixels(float)
    this
  }

  def above(another: UIBuilder, float: Float = 0): UIBuilder = {
    uiComponent.y = another.uiComponent.y - uiComponent.height - padding - toPixels(float)
    this
  }

  def below(another: UIBuilder, float: Float = 0): UIBuilder = {
    uiComponent.y = another.uiComponent.y + another.uiComponent.height + padding + toPixels(float)
    this
  }

  def leftOf(another: UIBuilder, float: Float = 0): UIBuilder = {
    uiComponent.x = another.uiComponent.x - uiComponent.width - padding - toPixels(float)
    this
  }

  def rightOf(another: UIBuilder, float: Float = 0): UIBuilder = {
    uiComponent.x = another.uiComponent.x + another.uiComponent.width + padding + toPixels(float)
    this
  }

  def xBetween(first: UIBuilder, second: UIBuilder) : UIBuilder = {
    val c1 = first.uiComponent
    val c2 = second.uiComponent
    uiComponent.x = c1.x + c1.width + (c1.x + c1.width - c2.x) / 2 - uiComponent.width / 2
    this
  }

  def yBetween(first: UIBuilder, second: UIBuilder) : UIBuilder = {
    val c1 = first.uiComponent
    val c2 = second.uiComponent
    uiComponent.y = c1.y + c1.height + (c1.y + c1.height - c2.y) / 2 - uiComponent.height / 2
    this
  }

  def drawable(drawable: Int) : UIBuilder = {
    uiComponent.drawable = drawable
    this
  }

  def id(string: String) : UIBuilder = {
    uiComponent.id = Some(string)
    this
  }

  def pad(paa: Int) : UIBuilder = padding(paa).pad()

  def pad() : UIBuilder = {
    uiComponent.x += padding
    uiComponent.y += padding
    uiComponent.width -= padding * 2
    uiComponent.height -= padding * 2
    this
  }

  def color1(int: Int) : UIBuilder = {
    uiComponent.color1 = int
    this
  }

  def color2(int: Int) : UIBuilder = {
    uiComponent.color2 = int
    this
  }

  def resolve = uiComponent

}
object UIBuilder {
  def apply(uIComponent: UIComponent) : UIBuilder = new UIBuilder(uIComponent)
}
object UIBuilderSettings {
  var padding = 0
}

