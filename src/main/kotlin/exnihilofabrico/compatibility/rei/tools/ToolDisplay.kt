package exnihilofabrico.compatibility.rei.tools

import exnihilofabrico.api.recipes.ToolRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModTools
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.util.Identifier

class ToolDisplay(val recipe: ToolRecipe, val category: Identifier): RecipeDisplay {
    override fun getRecipeCategory() = category

    override fun getOutputEntries() = recipe.lootables.map { it.stack.asREIEntry() }
    override fun getInputEntries(): MutableList<MutableList<EntryStack>>? {
        val ingredients = recipe.ingredient.asREIEntries()
        val tools =
            (if(recipeCategory == PluginEntry.HAMMER) ModTools.HAMMERS else ModTools.CROOKS)
                .values.map{ it.asREIEntry() }.toMutableList()

        return mutableListOf(ingredients, tools)
    }

}