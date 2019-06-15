package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.Lootable
import net.minecraft.recipe.Ingredient

data class SieveRecipe(
        val mesh: Ingredient,
        val fluid: FluidIngredient?,
        val sievable: Ingredient,
        val loot: MutableCollection<Lootable>
)