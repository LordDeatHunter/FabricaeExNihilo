package exnihilofabrico.api.crafting

import exnihilofabrico.api.crafting.FluidIngredient
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient

data class Lootable(
        val stack: ItemStack,
        val chance: List<Double>
) {
        constructor(stack: ItemStack, chance: Double): this(stack, listOf(chance))
}