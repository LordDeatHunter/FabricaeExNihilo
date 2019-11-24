package exnihilofabrico.compatibility.rei.tools

import exnihilofabrico.api.recipes.ToolRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModTools
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class ToolDisplay(val recipe: ToolRecipe, val category: Identifier): RecipeDisplay {
    override fun getRecipeCategory() = category

    override fun getOutput() = recipe.lootables.map { it.stack }
    override fun getInput(): MutableList<MutableList<ItemStack>>? {
        val ingredients = recipe.ingredient.flattenedListOfStacks()
        val tools =
            (if(recipeCategory == PluginEntry.HAMMER) ModTools.HAMMERS else ModTools.CROOKS)
                .values.map{ it.asStack() }.toMutableList()

        return mutableListOf(ingredients, tools)
    }

}