package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.EntityIngredient
import net.minecraft.fluid.Fluid

data class BarrelMilkingRecipe(
        val entity: EntityIngredient,
        val result: Fluid,
        val amount: Int,
        val coolDown: Int
)