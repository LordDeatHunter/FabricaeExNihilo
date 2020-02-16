package exnihilofabrico.util

import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity

object PlayerHelper {
    fun applyPotion(player: PlayerEntity, effect: StatusEffectInstance) {
        if(player.getStatusEffect(effect.effectType)?.duration ?: 0 <= effect.duration)
            player.addStatusEffect(effect)
    }
}