package com.tume.scalarpg.model.property


/**
  * Created by tume on 8/12/16.
  */
object Stat extends Enumeration {
  type Stat = Value
  val HealthFactor, ManaFactor, BaseDamageFactor, AccuracyFactor, AttackCritFactor, SpellCritFactor = Value
}
