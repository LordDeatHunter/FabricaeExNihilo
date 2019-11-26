package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.api.recipes.barrel.MilkingRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.ItemStack

class MilkingDisplay(val recipe: MilkingRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.MILKING

    override fun getOutput() = listOf(recipe.result.rawFluid?.bucketItem?.asStack() ?: ItemStack.EMPTY)
    override fun getInput() =
        listOf(recipe.entity.flattenListOfEggStacks(), ModBlocks.BARRELS.values.map { it.asStack() })
}