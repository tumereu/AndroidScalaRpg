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
    val lh = 0.06f // Stat & resistance label height
    val rw = 0.15f // Resistance label width
    val sw = 0.4f // Stat label width

    // Holder variables
    var lab = UIBuilder.label
    var first = UIBuilder.label
    var panel = UIBuilder.panel
    var heroSelect = UIBuilder.button
    var bt = UIBuilder.button

    // Hero and sign select buttons and stat panel
    view += {heroSelect = UIBuilder.button.size(0.21f).top().left().id("hero_select").img(R.drawable.hero_knight); heroSelect }
    heroSelect.tooltip("testitesti \n\nrivej' sana jotain jossain auto maatti ri vi n vaihto\n\n\n joko")
    view += { panel=UIBuilder.panel.width(0.79f).height(0.42f).top().right(); panel }
    view += UIBuilder.button.size(0.21f).alignBottom(panel).left().id("sign_select")
    // Name
    view += { lab=UIBuilder.label.text("Knight").height(0.08f).width(0.8f).alignLeft(panel, 0.02f).alignTop(panel, 0.005f); lab }
    // Stats
    view += { lab=UIBuilder.label.img(R.drawable.ic_heart).text("122").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab, 0.01f); first=lab; lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_mana).text("40").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_sword).text("2-5").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_staff).text("9").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_shoe).text("5").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab); lab }
    // Res row 1
    view += { lab=UIBuilder.label.img(R.drawable.ic_shield).text("5%").height(lh).width(rw).rightOf(first, 0.02f).alignTop(first); first=lab; lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_flame).text("2%").height(lh).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_frost).text("4%").height(lh).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_lightning).text("8%").height(lh).width(rw).alignLeft(lab).below(lab); lab }
    // Res row 2
    view += { lab=UIBuilder.label.img(R.drawable.ic_holy).text("7%").height(lh).width(rw).rightOf(first, 0.02f).alignTop(first); first=lab; lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_arcane).text("7%").height(lh).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_poison).text("1%").height(lh).width(rw).alignLeft(lab).below(lab); lab }
    view += { lab=UIBuilder.label.img(R.drawable.ic_dark).text("3%").height(lh).width(rw).alignLeft(lab).below(lab); lab }
    // Equipment
    view += { bt=UIBuilder.button.size(0.15f).below(panel).alignLeft(panel).id("main_weapon_select").img(R.drawable.wpn_2hsword_0); bt }
    view += { bt=UIBuilder.button.size(0.15f).below(panel).rightOf(bt).id("off_weapon_select").img(R.drawable.wpn_shield_0); bt }
    view += { bt=UIBuilder.button.size(0.15f).below(panel).rightOf(bt).id("armor_select").img(R.drawable.armor_0); bt }
    view += { bt=UIBuilder.button.size(0.15f).below(panel).rightOf(bt).id("trinket_select").img(R.drawable.trinket_0); bt }

    view.foreach(_.pad())
    view
  }
}
