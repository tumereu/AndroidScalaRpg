package com.tume.scalarpg.ui

import com.tume.engine.gui.UIView
import com.tume.engine.gui.builder.UIBuilder

import scala.collection.mutable

/**
  * Created by tume on 8/8/16.
  */
class HeroUI extends UIView {
  override def name: String = "HeroUI"

  override def build: Seq[UIBuilder] = {
    var view = mutable.Buffer[UIBuilder]()
    var tmp: UIBuilder = UIBuilder.button
    view += { tmp = UIBuilder.button.size(0.2f).top().left().id("hero0"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).top().rightOf(tmp).id("hero1"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).top().rightOf(tmp).id("hero2"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).top().rightOf(tmp).id("hero3"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).top().rightOf(tmp).id("hero4"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).below(tmp).left().id("hero5"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero6"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero7"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero8"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero9"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).below(tmp).left().id("hero10"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero11"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero12"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero13"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero14"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).below(tmp).left().id("hero15"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero16"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero17"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero18"); tmp }
    view += { tmp = UIBuilder.button.size(0.2f).alignTop(tmp).rightOf(tmp).id("hero19"); tmp }
    view.foreach(_.pad())
    view
  }
}
