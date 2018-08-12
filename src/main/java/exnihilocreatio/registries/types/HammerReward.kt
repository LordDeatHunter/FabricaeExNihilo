package exnihilocreatio.registries.types

import lombok.AllArgsConstructor
import net.minecraft.item.ItemStack

@AllArgsConstructor
data class HammerReward(
        val stack: ItemStack? = null,
        val miningLevel: Int = 0,
        val chance: Float = 0.0f,
        val fortuneChance: Float = 0.0f)
