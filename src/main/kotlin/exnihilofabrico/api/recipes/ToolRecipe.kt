package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import net.minecraft.item.Item

data class ToolRecipe(val ingredient:TagIngredient<Item> = TagIngredient(),
                      val lootables: MutableList<Lootable> = mutableListOf())