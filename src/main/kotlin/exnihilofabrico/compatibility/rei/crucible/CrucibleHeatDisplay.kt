package exnihilofabrico.compatibility.rei.crucible

import exnihilofabrico.api.recipes.crucible.CrucibleHeatRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.util.asREIEntry
import exnihilofabrico.util.getExNihiloItemStack
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay

class CrucibleHeatDisplay(val recipe: CrucibleHeatRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.CRUCIBLE_HEAT

    override fun getOutputEntries() = listOf<EntryStack>()
    override fun getInputEntries() =
        listOf(
            listOf(recipe.ingredient.asREIEntries(), recipe.fluid.asREIEntries()).flatten(),
            listOf(getExNihiloItemStack("stone_crucible").asREIEntry()))
    fun getHeat() = recipe.value

}