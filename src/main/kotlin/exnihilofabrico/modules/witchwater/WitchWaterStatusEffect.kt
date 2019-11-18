package exnihilofabrico.modules.witchwater

import exnihilofabrico.util.Color
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType

object WitchWaterStatusEffect: StatusEffect(StatusEffectType.NEUTRAL, Color.DARK_PURPLE.toInt()) {

    fun getInstance(): StatusEffectInstance {
        val instance = StatusEffectInstance(this, 72000, 1, false, false, false)
        instance.isPermanent = true

        return instance
    }

}