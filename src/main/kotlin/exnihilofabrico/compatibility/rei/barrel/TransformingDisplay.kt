package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.api.recipes.barrel.FluidTransformRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay

class TransformingDisplay(val recipe: FluidTransformRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.TRANSFORMING

    override fun getOutputEntries() = listOf(
        when(recipe.result) {
            is ItemMode -> EntryStack.create(recipe.result.stack)
            is FluidMode -> recipe.result.fluid.rawFluid?.bucketItem?.asREIEntry() ?: EntryStack.empty()
            else -> EntryStack.empty()
        }
    )
    override fun getInputEntries() =
        listOf(recipe.inBarrel.asREIEntries(), recipe.catalyst.asREIEntries(), ModBlocks.BARRELS.values.map { it.asREIEntry() })

}