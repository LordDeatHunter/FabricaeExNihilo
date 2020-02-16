package exnihilofabrico.compatibility.rei.witchwater

import exnihilofabrico.api.recipes.witchwater.WitchWaterWorldRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.RecipeDisplay

class WitchWaterWorldDisplay(val recipe: WitchWaterWorldRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.WITCH_WATER_WORLD

    override fun getOutputEntries() = recipe.results.asEntryList()
    override fun getInputEntries() =
        listOf(listOf(WitchWaterFluid.bucket.asREIEntry()), recipe.fluid.asREIEntries())
}