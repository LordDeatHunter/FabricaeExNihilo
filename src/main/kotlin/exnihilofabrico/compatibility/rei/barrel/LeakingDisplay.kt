package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.api.recipes.barrel.LeakingRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.block.Material

class LeakingDisplay(val recipe: LeakingRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.LEAKING

    override fun getOutput() = listOf(recipe.result.asStack())
    override fun getInput() =
        listOf(recipe.target.flattenedListOfStacks(),
            recipe.fluid.flattenListOfBuckets(),
            ModBlocks.BARRELS.values.filter { it.getMaterial(it.defaultState) != Material.STONE } .map { it.asStack() })

}