package exnihilofabrico.modules

import exnihilofabrico.id
import exnihilofabrico.modules.witchwater.WitchWaterStatusEffect
import exnihilofabrico.util.Color
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.util.registry.Registry

object ModEffects {
    /**
     * A status effect used to mark entities that have been processed by the witch water code so that they are no longer processed.
     */
    val WITCHWATERED: StatusEffect = WitchWaterStatusEffect
    val MILKED = object: StatusEffect(StatusEffectType.NEUTRAL, Color.WHITE.toInt()) {}

    fun registerEffects(registry: Registry<StatusEffect>) {
        Registry.register(registry, id("witchwatered"), WITCHWATERED)
        Registry.register(registry, id("milked"), MILKED)
    }
}