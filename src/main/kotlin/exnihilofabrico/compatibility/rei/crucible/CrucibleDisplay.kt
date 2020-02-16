package exnihilofabrico.compatibility.rei.crucible

import exnihilofabrico.api.recipes.crucible.CrucibleRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.block.Material
import net.minecraft.util.Identifier

class CrucibleDisplay(val recipe: CrucibleRecipe, val category: Identifier): RecipeDisplay {
    override fun getRecipeCategory() = category

    override fun getOutputEntries() = listOf(recipe.output.rawFluid?.bucketItem?.asREIEntry() ?: EntryStack.empty())
    override fun getInputEntries() =
        listOf(
            recipe.input.asREIEntries(),
            ModBlocks.CRUCIBLES.values.filter{
                if(category == PluginEntry.WOOD_CRUCIBLE)
                    it.getMaterial(it.defaultState) == Material.WOOD
                else
                    it.getMaterial(it.defaultState) == Material.STONE
            }.map { it.asREIEntry() }
        )

}