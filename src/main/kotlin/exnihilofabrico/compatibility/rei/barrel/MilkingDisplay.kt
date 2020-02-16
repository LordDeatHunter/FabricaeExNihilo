package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.api.recipes.barrel.MilkingRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay

class MilkingDisplay(val recipe: MilkingRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.MILKING

    override fun getOutputEntries() = listOf(recipe.result.rawFluid?.bucketItem?.asREIEntry() ?: EntryStack.empty())
    override fun getInputEntries() =
        listOf(recipe.entity.asREIEntries(), ModBlocks.BARRELS.values.map { it.asREIEntry() })
}