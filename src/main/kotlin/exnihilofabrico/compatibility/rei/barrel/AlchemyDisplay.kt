package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.api.recipes.barrel.AlchemyRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem

class AlchemyDisplay(val recipe: AlchemyRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.ALCHEMY

    override fun getOutput() = listOf(
        recipe.product.let { mode ->
            when(mode){
                is ItemMode -> mode.stack
                is FluidMode -> mode.fluid.rawFluid?.bucketItem?.asStack() ?: ItemStack.EMPTY
                else -> ItemStack.EMPTY
            }
        },
        recipe.byproduct.stack,
        if(!recipe.toSpawn.isEmpty())
            SpawnEggItem.forEntity(recipe.toSpawn.type).asStack()
        else
            ItemStack.EMPTY
    )
    override fun getInput() =
        listOf(recipe.reactant.flattenListOfBuckets(), recipe.catalyst.flattenedListOfStacks(), ModBlocks.BARRELS.values.map { it.asStack() })

}