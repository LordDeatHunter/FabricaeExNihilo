package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.crafting.Lootable
import net.minecraft.recipe.Ingredient

data class BarrelAlchemyRecipe<T>(
        val reactant: Ingredient,
        val catalyst: Ingredient,
        val product: T,
        val byproduct: Lootable,
        val toSpawn: EntityStack
)