package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.util.asREIEntry
import net.minecraft.item.ItemStack

// Holds a reversed/composite view of the compost recipes; i.e. many -> one
data class REICompostRecipe(val inputs: List<ItemStack> = listOf(), val output: ItemStack = ItemStack.EMPTY) {
    fun reiInputs() = inputs.map { it.asREIEntry() }
    fun reiOutput() = output.asREIEntry()
}