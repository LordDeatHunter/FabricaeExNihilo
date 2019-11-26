package exnihilofabrico.compatibility.rei.witchwater

import exnihilofabrico.api.recipes.witchwater.WitchWaterEntityRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.asStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem

class WitchWaterEntityDisplay(val recipe: WitchWaterEntityRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.WITCH_WATER_ENTITY

    override fun getOutput() = listOf(SpawnEggItem.forEntity(recipe.tospawn)?.asStack() ?: ItemStack.EMPTY)
    override fun getInput() =
        listOf(recipe.target.flattenListOfEggStacks(), listOf(WitchWaterFluid.bucket.asStack()))
}