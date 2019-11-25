package exnihilofabrico.compatibility.rei.crucible

import exnihilofabrico.api.recipes.crucible.CrucibleRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.block.Material
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class CrucibleDisplay(val recipe: CrucibleRecipe, val category: Identifier): RecipeDisplay {
    override fun getRecipeCategory() = category

    override fun getOutput() = listOf(recipe.output.rawFluid?.bucketItem?.asStack() ?: ItemStack.EMPTY)
    override fun getInput() =
        listOf(
            recipe.input.flattenedListOfStacks(),
            ModBlocks.CRUCIBLES.values.filter{
                if(category == PluginEntry.WOOD_CRUCIBLE)
                    it.getMaterial(it.defaultState) == Material.WOOD
                else
                    it.getMaterial(it.defaultState) == Material.STONE
            }.map { it.asStack() }
        )

}