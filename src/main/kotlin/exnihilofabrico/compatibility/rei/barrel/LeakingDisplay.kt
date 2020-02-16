package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.api.recipes.barrel.LeakingRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.block.Material

class LeakingDisplay(val recipe: LeakingRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.LEAKING

    override fun getOutputEntries() = listOf(recipe.result.asREIEntry())
    override fun getInputEntries() =
        listOf(recipe.target.asREIEntries(),
            recipe.fluid.asREIEntries(),
            ModBlocks.BARRELS.values.filter { it.getMaterial(it.defaultState) != Material.STONE } .map { it.asREIEntry() })

}