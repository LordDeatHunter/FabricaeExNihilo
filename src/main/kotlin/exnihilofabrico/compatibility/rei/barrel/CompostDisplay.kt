package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay

class CompostDisplay(val recipe: REICompostRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.COMPOSTING

    override fun getOutput() = listOf(recipe.output)
    override fun getInput() =
        listOf(recipe.inputs, ModBlocks.BARRELS.values.map { it.asStack() })

}