package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.Lootable
import net.minecraft.recipe.Ingredient

data class ToolRecipe(val ingredient: Ingredient = Ingredient.EMPTY, val lootables: MutableList<Lootable> = mutableListOf()) {

}