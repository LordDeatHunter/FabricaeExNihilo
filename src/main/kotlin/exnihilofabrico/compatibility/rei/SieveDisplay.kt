package exnihilofabrico.compatibility.rei

import exnihilofabrico.api.recipes.SieveRecipe
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.ItemStack

class SieveDisplay(val recipe: SieveRecipe): RecipeDisplay {
    override fun getRecipeCategory() = SieveCategory.CATEGORY_ID

    override fun getOutput() = recipe.loot.map { it.stack }.toMutableList()
    override fun getInput() =
        mutableListOf(
            // first is the mesh(s)
            recipe.mesh.stackArray.toMutableList(),
            // second is the fluid(s) as buckets
            recipe.fluid?.toItemStacks()?.toMutableList() ?: mutableListOf<ItemStack>(),
            // third is the to sieve block(s)
            recipe.sievable.stackArray.toMutableList())

}