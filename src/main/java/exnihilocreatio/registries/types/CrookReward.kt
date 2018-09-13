package exnihilocreatio.registries.types

import net.minecraft.item.ItemStack


// TODO: MOVE TO API PACKAGE ON BREAKING VERSION CHANGE
data class CrookReward(
        val stack: ItemStack? = null,
        val chance: Float = 0.0f,
        val fortuneChance: Float = 0.0f
)



