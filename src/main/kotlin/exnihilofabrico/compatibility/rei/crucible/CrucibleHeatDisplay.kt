package exnihilofabrico.compatibility.rei.crucible

import exnihilofabrico.api.recipes.crucible.CrucibleHeatRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.util.getExNihiloItemStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.ItemStack

class CrucibleHeatDisplay(val recipe: CrucibleHeatRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.CRUCIBLE_HEAT

    override fun getOutput() = listOf<ItemStack>()
    override fun getInput() =
        listOf(
            listOf(recipe.ingredient.flattenedListOfStacks(), recipe.fluid.flattenListOfBuckets()).flatten(),
            listOf(getExNihiloItemStack("stone_crucible")))
    fun getHeat() = recipe.value

}