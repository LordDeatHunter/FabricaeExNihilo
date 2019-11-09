package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.modules.barrels.BarrelMode
import net.minecraft.recipe.Ingredient

data class BarrelAlchemyRecipe(
        val reactant: Ingredient,
        val catalyst: Ingredient,
        val product: BarrelMode,
        val byproduct: Lootable,
        val toSpawn: EntityStack
)