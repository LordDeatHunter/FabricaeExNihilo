package exnihilofabrico.compatibility.rei.witchwater

import exnihilofabrico.api.recipes.witchwater.WitchWaterEntityRecipe
import exnihilofabrico.compatibility.rei.PluginEntry
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.asREIEntry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeDisplay
import net.minecraft.item.SpawnEggItem

class WitchWaterEntityDisplay(val recipe: WitchWaterEntityRecipe): RecipeDisplay {
    override fun getRecipeCategory() = PluginEntry.WITCH_WATER_ENTITY

    override fun getOutputEntries() = listOf(SpawnEggItem.forEntity(recipe.tospawn)?.asREIEntry() ?: EntryStack.empty())
    override fun getInputEntries() =
        listOf(recipe.target.asREIEntries(), listOf(WitchWaterFluid.bucket.asREIEntry()))
}