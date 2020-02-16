package exnihilofabrico.compatibility.rei.barrel

import exnihilofabrico.api.recipes.barrel.AlchemyRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.SpawnEggItem

class AlchemyDisplay(val recipe: AlchemyRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.ALCHEMY

    override fun getOutputEntries() = listOf(
        recipe.product.let { mode ->
            when(mode){
                is ItemMode -> mode.stack.asREIEntry()
                is FluidMode -> mode.fluid.rawFluid?.bucketItem?.asREIEntry() ?: EntryStack.empty()
                else -> EntryStack.empty()
            }
        },
        recipe.byproduct.stack.asREIEntry(),
        if(!recipe.toSpawn.isEmpty())
            SpawnEggItem.forEntity(recipe.toSpawn.type)?.asREIEntry()
        else
            EntryStack.empty()
    )
    override fun getInputEntries() =
        listOf(recipe.reactant.asREIEntries(), recipe.catalyst.asREIEntries(), ModBlocks.BARRELS.values.map { it.asREIEntry() })

}