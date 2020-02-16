package exnihilofabrico.compatibility.rei.sieve

import exnihilofabrico.api.recipes.SieveRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay

class SieveDisplay(val recipe: SieveRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.SIEVE

    override fun getOutputEntries() = recipe.loot.map { it.stack.asREIEntry() }.toMutableList()
    override fun getInputEntries(): List<List<EntryStack>> {
        val sievable = recipe.sievable.asREIEntries()
        val mesh = recipe.mesh.asREIEntries()
        val buckets = recipe.fluid.asREIEntries()
        val sieves = ModBlocks.SIEVES.values.map { it.asREIEntry() }.toMutableList()

        return mutableListOf(sievable, mesh, buckets, sieves)
    }

}