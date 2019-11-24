package exnihilofabrico.compatibility.rei

import exnihilofabrico.api.recipes.SieveRecipe
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.ItemStack

class SieveDisplay(val recipe: SieveRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.SIEVE

    override fun getOutput() = recipe.loot.map { it.stack }.toMutableList()
    override fun getInput(): MutableList<MutableList<ItemStack>>? {
        val sievable = recipe.sievable.flattenedListOfStacks()
        val mesh = recipe.mesh.flattenedListOfStacks()
        val buckets = recipe.fluid.flattenListOfBuckets()
        val sieves = ModBlocks.SIEVES.values.map { it.asStack() }.toMutableList()

        return mutableListOf(sievable, mesh, buckets, sieves)
    }

}