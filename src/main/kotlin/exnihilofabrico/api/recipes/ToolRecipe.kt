package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable

data class ToolRecipe(val ingredient: ItemIngredient = ItemIngredient.EMPTY,
                      val lootables: MutableList<Lootable> = mutableListOf())