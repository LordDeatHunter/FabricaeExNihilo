package exnihilofabrico.compatibility.rei.witchwater

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.recipes.witchwater.WitchWaterWorldRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay

class WitchWaterWorldDisplay(val recipe: WitchWaterWorldRecipe): RecipeDisplay {
    init {
        ExNihiloFabrico.LOGGER.info("Registering WWWDisplay: ${recipe.fluid}")
    }

    override fun getRecipeCategory() = PluginEntry.WITCH_WATER_WORLD

    override fun getOutput() = recipe.results.asListOfStacks()
    override fun getInput() =
        listOf(listOf(WitchWaterFluid.bucket.asStack()), recipe.fluid.flattenListOfBuckets())
}