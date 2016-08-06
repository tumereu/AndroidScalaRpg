package com.tume.engine.anim

import android.util.Log
import com.tume.engine.anim.LoopType.LoopType
import com.tume.engine.util.{Calc, Timer}

/**
  * Created by tume on 8/1/16.
  */
abstract class Animation(private[anim] val duration: Float, private[anim] val loopType: LoopType) {

  var onFinish = () => {}

  Animations.register(this)
  import com.tume.engine.anim.LoopType._

  protected var counter : Float = 0f

  protected var finished = false
  protected var direction = 1f
  protected var paused = false
  protected var timer = Option[Timer](null)

  private var reversed = false

  private[anim] def update(delta: Float): Unit = {
    counter += direction * delta / duration
    if (counter > 1 || counter < 0) {
      counter = Calc.clamp(counter, 0, 1)
      animationFinished()
    }
    if (timer.isDefined && timer.get.tick(delta)) {
      finished = true
    }
  }

  def pause() : Unit = paused = true
  def resume() : Unit = paused = false
  def reset() : Unit = {
    finished = false
    paused = false
    counter = 0f
    timer.foreach(_.reset())
    Animations.register(this)
  }

  private def animationFinished(): Unit = {
    loopType match {
      case Once => finished = true
      case Repeat => counter = 0f
      case Reverse => direction *= -1f
    }
    onFinish()
  }

  protected def t = counter
  private[anim] def v: Float

  def value : Float = if (reversed) 1f - v else v
  def value(i: Int): Int = (value * i).toInt
  def reverse: Animation = {
    this.reversed = true
    this
  }
  def isFinished = finished

  private[anim] def isRemovable = finished

}
object Animations {
  protected var anims = Vector.empty[Animation]

  def update(delta: Float): Unit = {
    anims.foreach(_.update(delta))
    anims = anims.filterNot(_.isRemovable)
  }

  def register(animation: Animation): Unit = {
    anims = anims.filterNot(_ == animation) :+ animation
  }
}
object LoopType extends Enumeration {
  type LoopType = Value
  val Repeat, Reverse, Once = Value
}
case class EmptyAnim() extends Animation(1, LoopType.Once) {
  override def v = 1f
  override def isRemovable = true
}
case class LinearAnim(dur: Float, lt: LoopType) extends Animation(dur, lt) {
  override def v = t
}
case class QuinticOutAnim(dur: Float, lt: LoopType) extends Animation(dur, lt) {
  override def v = Calc.pow(t - 1f, 5f) + 1
}
