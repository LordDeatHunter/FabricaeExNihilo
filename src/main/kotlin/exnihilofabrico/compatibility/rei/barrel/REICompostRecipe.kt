package exnihilofabrico.compatibility.rei.barrel

import net.minecraft.item.ItemStack

// Holds a reversed/composite view of the compost recipes; i.e. many -> one
data class REICompostRecipe(val inputs: List<ItemStack> = listOf(), val output: ItemStack = ItemStack.EMPTY)