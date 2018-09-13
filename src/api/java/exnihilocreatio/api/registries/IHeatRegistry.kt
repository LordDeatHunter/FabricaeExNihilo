package exnihilocreatio.api.registries

import exnihilocreatio.util.BlockInfo
import net.minecraft.item.ItemStack

interface IHeatRegistry : IRegistryMap<BlockInfo, Int> {
    fun register(stack: ItemStack, heatAmount: Int)

    fun getHeatAmount(stack: ItemStack): Int
    fun getHeatAmount(info: BlockInfo): Int
}
