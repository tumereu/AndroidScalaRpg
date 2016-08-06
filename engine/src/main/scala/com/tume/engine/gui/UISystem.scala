package com.tume.engine.gui

import android.graphics.Canvas
import android.util.Log
import com.tume.engine.Input
import com.tume.engine.gui.event.UIEventListener
import com.tume.engine.util.D

/**
  * Created by tume on 5/12/16.
  */
class UISystem {

  var components = Map[String, Vector[UIComponent]]()
  var activeComponents = Vector[UIComponent]()

  var activePopups = Vector.empty[UIPopupPanel]

  def init(views: Seq[UIView], listener: UIEventListener): Unit = {
    for (v <- views) {
      val vec = Vector.newBuilder[UIComponent]
      for (builder <- v.build) {
        vec += builder.resolve
      }
      val vecRes = vec.result()
      vecRes.foreach(_.view = Some(v.name))
      components += v.name -> vecRes
    }
    for (cmp <- components.values.flatten) {
      cmp.uiSystem = this
      cmp.listener = Some(listener)
      cmp.toggleVisibility(false)
      cmp.init()
    }
    if (views.nonEmpty) {
      this.show(views.head.name)
    }
  }

  def update(delta: Float): Unit = {
    var toBeRemoved : Option[UIComponent] = None
    if (activePopups.nonEmpty) {
      val a = this.activePopups.head
      if (Input.tap && !Input.tap(a.boundingBox)) {
        a.onToBeRemoved()
      }
      if (a.isReadyToBeRemoved) {
        toBeRemoved = Some(a)
      }
    }
    for (cmp <- components.values.flatten) {
      try {
        cmp.update(delta)
      } catch {
        case e: Exception => {
          Log.e(getClass.toString, "Exception when updating ui component " + cmp.id.getOrElse(""), e)
          System.exit(0)
        }
      }
    }
    toBeRemoved.foreach(removePopup)
  }

  def render(canvas: Canvas): Unit = {
    val layered = activeComponents.groupBy(_.layer)
    for (key <- layered.keys.toVector.sorted) {
      for (cmp <- layered(key).filter(_.visible)) {
        canvas.save()
        canvas.translate(cmp.x, cmp.y)
        canvas.clipRect(0, 0, cmp.width, cmp.height)
        cmp.render(canvas)
        canvas.restore()
      }
    }
  }

  def show(views: String*): Unit = {
    for (cmp <- this.activeComponents) {
      cmp.toggleVisibility(false)
    }
    this.activeComponents = Vector.empty
    for (s <- views) {
      this.activeComponents = this.activeComponents ++ this.components(s)
    }
    for (cmp <- this.activeComponents) {
      cmp.onViewShow()
    }
  }

  def findComponent(id: String) : Option[UIComponent] = {
    var found : Option[UIComponent] = None
    for (c <- this.components.values.flatten) {
      val f = c.find(id)
      if (f.isDefined) {
        found = f
      }
    }
    found
  }

  def isReceivingInput(uIComponent: UIComponent): Boolean = {
    if (activePopups.isEmpty) {
      true
    } else {
      activePopups.head.contains(uIComponent)
    }
  }

  def addPopup(uIComponent: UIPopupPanel): Unit = {
    activePopups = activePopups :+ uIComponent
    uIComponent.onAddAsPopup(activePopups)
  }

  def removePopup(uIComponent: UIComponent): Unit = {
    activePopups = activePopups.filterNot(_ == uIComponent)
    uIComponent.onRemoveAsPopup(activePopups)
  }

}
