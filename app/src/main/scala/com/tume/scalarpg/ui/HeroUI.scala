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
    val sw = 0.2f // Stat label width
    val navS = 0.125f //Stage nav button size

    // Holder variables
    var lab = UIBuilder.label
    var first = UIBuilder.label
    var panel = UIBuilder.panel
    var heroSelect = UIBuilder.button
    var bt = UIBuilder.button
    var name = UIBuilder.label
    var stage = UIBuilder.panel

    // Hero and sign select buttons and stat panel
    view += {heroSelect = UIBuilder.button.size(0.35f/2).top().left().id("hero_select").img(R.drawable.hero_warrior); heroSelect }
    view += { panel=UIBuilder.panel.width(1f-0.35f/2).height(0.35f).top().right(); panel }
    view += UIBuilder.button.size(0.35f/2).alignBottom(panel).left().id("sign_select")
    // Name
    view += { lab=UIBuilder.label.text("Warrior").height(0.08f).width(0.8f).alignLeft(panel, 0.02f).alignTop(panel, 0.005f); name=lab; lab }
    // Stats row 1
    view += { lab=UIBuilder.label.img(R.drawable.ic_heart).text("122").height(lh).width(sw).alignLeft(panel, 0.02f).below(name, 0.01f); first=lab; lab }.id("info_health")
    view += { lab=UIBuilder.label.img(R.drawable.ic_sword).text("2-5").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab); lab }.id("info_damage")
    view += { lab=UIBuilder.label.img(R.drawable.ic_crosshair).text("80%").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab); lab }.id("info_accuracy")
    view += { lab=UIBuilder.label.img(R.drawable.ic_staff).text("9").height(lh).width(sw).alignLeft(panel, 0.02f).below(lab); lab }.id("info_ability_power")
    // Stats row 2
    view += { lab=UIBuilder.label.img(R.drawable.ic_mana).text("40").height(lh).width(sw).rightOf(first, 0.02f).alignTop(first); first=lab; lab }.id("info_mana")
    view += { lab=UIBuilder.label.img(R.drawable.ic_daggers).text("3.2").height(lh).width(sw).alignLeft(lab).below(lab); lab }.id("info_attack_speed")
    view += { lab=UIBuilder.label.img(R.drawable.ic_critical_hit).text("6.7%").height(lh).width(sw).alignLeft(lab).below(lab); lab }.id("info_crit_chance")
    view += { lab=UIBuilder.label.img(R.drawable.ic_shoe).text("5").height(lh).width(sw).alignLeft(lab).below(lab); lab }.id("info_speed")
    // Res row 1
    view += { lab=UIBuilder.label.img(R.drawable.ic_shield).text("5.4%").height(lh).width(rw).rightOf(first, 0.02f).alignTop(first); first=lab; lab }.id("res_physical")
    view += { lab=UIBuilder.label.img(R.drawable.ic_flame).text("2.2%").height(lh).width(rw).alignLeft(lab).below(lab); lab }.id("res_fire")
    view += { lab=UIBuilder.label.img(R.drawable.ic_frost).text("4.9%").height(lh).width(rw).alignLeft(lab).below(lab); lab }.id("res_frost")
    view += { lab=UIBuilder.label.img(R.drawable.ic_lightning).text("8.0%").height(lh).width(rw).alignLeft(lab).below(lab); lab }.id("res_lightning")
    // Res row 2
    view += { lab=UIBuilder.label.img(R.drawable.ic_holy).text("7.0%").height(lh).width(rw).rightOf(first, 0.02f).alignTop(first); first=lab; lab }.id("res_holy")
    view += { lab=UIBuilder.label.img(R.drawable.ic_arcane).text("7.6%").height(lh).width(rw).alignLeft(lab).below(lab); lab }.id("res_arcane")
    view += { lab=UIBuilder.label.img(R.drawable.ic_poison).text("1.8%").height(lh).width(rw).alignLeft(lab).below(lab); lab }.id("res_poison")
    view += { lab=UIBuilder.label.img(R.drawable.ic_dark).text("3.1%").height(lh).width(rw).alignLeft(lab).below(lab); lab }.id("res_dark")
    // Equipment
    view += { bt=UIBuilder.button.size(0.15f).below(panel, 0.05f).left(0.05f).id("main_weapon_select"); bt }
    view += { bt=UIBuilder.button.size(0.15f).alignTop(bt).rightOf(bt).id("off_weapon_select"); bt }
    view += { bt=UIBuilder.button.size(0.15f).alignTop(bt).rightOf(bt).id("helmet_select"); bt }
    view += { bt=UIBuilder.button.size(0.15f).alignTop(bt).rightOf(bt).id("armor_select"); bt }
    view += { bt=UIBuilder.button.size(0.15f).alignTop(bt).rightOf(bt).id("boots_select"); bt }
    view += { bt=UIBuilder.button.size(0.15f).alignTop(bt).rightOf(bt).id("trinket_select"); bt }
    // Stage select
    view += {stage=UIBuilder.panel.absWidth(0.90f).height(0.4f).below(bt, 0.05f).left(0.05f).id("stage_select_panel"); stage }
    view += UIBuilder.button.size(navS).alignCenter(stage).leftOf(stage, -navS / 3 * 2).img(R.mipmap.ic_arrow_left)
    view += UIBuilder.button.size(navS).alignCenter(stage).rightOf(stage, -navS / 3 * 2).img(R.mipmap.ic_arrow_right)
    view.foreach(_.pad())
    view
  }
}
