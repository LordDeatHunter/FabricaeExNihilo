package exnihilofabrico.client

import exnihilofabrico.common.base.IHasColor
import exnihilofabrico.common.ore.OreChunkItem
import exnihilofabrico.common.ore.OrePieceItem
import exnihilofabrico.util.Color
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.ItemStack

object ExNihiloItemColorProvider: ItemColorProvider {
    override fun getColor(stack: ItemStack, index: Int): Int {
        return when(val item = stack.item) {
            is IHasColor -> item.getColor(index)
            else -> Color.WHITE.toIntIgnoreAlpha()
        }

    }

}