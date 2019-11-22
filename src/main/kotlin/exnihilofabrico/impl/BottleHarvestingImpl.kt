package exnihilofabrico.impl

import exnihilofabrico.util.getExNihiloItem
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack

object BottleHarvestingImpl {

    fun getResult(target: BlockState): ItemStack {
        return if(target.block != Blocks.SAND)
            ItemStack.EMPTY
        else
            ItemStack(getExNihiloItem("salt"))
    }

}