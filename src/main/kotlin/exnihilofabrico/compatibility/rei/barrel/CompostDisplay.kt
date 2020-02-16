package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.RecipeDisplay

class CompostDisplay(val recipe: REICompostRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.COMPOSTING

    override fun getOutputEntries() = listOf(recipe.reiOutput())
    override fun getInputEntries() =
        listOf(recipe.reiInputs(), ModBlocks.BARRELS.values.map { it.asREIEntry() })

}