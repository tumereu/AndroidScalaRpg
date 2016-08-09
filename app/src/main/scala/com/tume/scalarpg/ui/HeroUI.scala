package com.tume.scalarpg.ui

import com.tume.engine.gui.UIView
import com.tume.engine.gui.builder.UIBuilder
import com.tume.scalarpg.R

import scala.collection.mutable

/**
  * Created by tume on 8/8/16.
  */
class HeroUI extends UIView {
  override def name: String = "HeroUI"

  override def build: Seq[UIBuilder] = {
    var view = mutable.Buffer[UIBuilder]()
    val ls = 0.06f // Stat & resistance label height
    val rw = 0.15f // Resistance label width
    val sw = 0.4f // Stat label width

    var lab = UIBuilder.label
    var first = UIBuilder.label
    var panel = UIBuilder.panel
    view += UIBuilder.button.size(0.2f).top().left().id("hero_select")
    view += { panel=UIBuilder.panel.width(0.8f).height(0.42f).top().right(); panel }
    // Name
    view += { lab=UIBuilder.label.text("Knight").height(0.08f).width(0.8f).alignLeft(panel, 0.02f).alignTop(panel, 0.005f); lab }
    // Stats
    view += { lab=UIBuilder.label.img(R.drawable.ic_heart).text("122").height(ls).width(sw).alignLeft(panel, 0.02f).below(lab, 0.01f); first=lab; lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_mana).text("40").height(ls).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_sword).text("2-5").height(ls).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_staff).text("9").height(ls).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_shoe).text("5").height(ls).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    // Res row 1
    view += { lab=UIBuilder.label.img(R.drawable.ic_shield).text("5%").height(ls).width(rw).rightOf(first, 0.02f).alignTop(first); first=lab; lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_flame).text("2%").height(ls).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_frost).text("4%").height(ls).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_lightning).text("8%").height(ls).width(rw).alignLeft(lab).below(lab); lab }
    // Res row 2
    view += { lab=UIBuilder.label.img(R.drawable.ic_holy).text("7%").height(ls).width(rw).rightOf(first, 0.02f).alignTop(first); first=lab; lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_arcane).text("7%").height(ls).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_poison).text("1%").height(ls).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_dark).text("3%").height(ls).width(rw).alignLeft(lab).below(lab); lab }

    view.foreach(_.pad())
    view
  }
}
