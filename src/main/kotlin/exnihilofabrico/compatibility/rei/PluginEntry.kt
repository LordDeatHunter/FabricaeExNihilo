package exnihilofabrico.compatibility.rei

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.compatibility.rei.sieve.SieveCategory
import exnihilofabrico.compatibility.rei.sieve.SieveDisplay
import exnihilofabrico.compatibility.rei.tools.ToolCategory
import exnihilofabrico.compatibility.rei.tools.ToolDisplay
import exnihilofabrico.id
import exnihilofabrico.util.getExNihiloItemStack
import me.shedaniel.math.api.Rectangle
import me.shedaniel.rei.api.RecipeHelper
import me.shedaniel.rei.api.plugins.REIPluginV0
import net.fabricmc.loader.api.SemanticVersion

class PluginEntry: REIPluginV0 {

    override fun getPluginIdentifier() = PLUGIN
    override fun getMinimumVersion(): SemanticVersion = SemanticVersion.parse("3.0-pre")

    override fun registerPluginCategories(helper: RecipeHelper) {
        ExNihiloFabrico.LOGGER.info("Registering REI Categories")
        helper.registerCategory(SieveCategory())
        helper.registerCategory(ToolCategory(CROOK, getExNihiloItemStack("crook_wood"), "Crook"))
        helper.registerCategory(ToolCategory(HAMMER, getExNihiloItemStack("hammer_wood"), "Hammer"))

        // Hackishly Remove the autocrafting button
        helper.registerAutoCraftButtonArea(SIEVE) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(CROOK) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(HAMMER) {_ -> Rectangle(0,0,0,0) }
    }

    override fun registerRecipeDisplays(helper: RecipeHelper) {
        ExNihiloFabrico.LOGGER.info("Registering REI Displays")
        ExNihiloRegistries.SIEVE.getREIRecipes().forEach { helper.registerDisplay(SIEVE, SieveDisplay(it)) }
        ExNihiloRegistries.CROOK.getREIRecipes().forEach { helper.registerDisplay(CROOK, ToolDisplay(it, CROOK)) }
        ExNihiloRegistries.HAMMER.getREIRecipes().forEach { helper.registerDisplay(HAMMER, ToolDisplay(it, HAMMER)) }

    }

    companion object {

        val PLUGIN = id("rei")

        val SIEVE = id("rei/sieve")

        val ALCHEMY = id("rei/barrel/alchemy")
        val COMPOSTING= id("rei/barrel/composting")
        val LEAKING= id("rei/barrel/leaking")
        val BLEEDING= id("rei/barrel/bleeding")
        val MILKING= id("rei/barrel/milking")
        val TRANSFORMING= id("rei/barrel/transforming")

        val WOOD_CRUCIBLE = id("rei/crucible/wood")
        val STONE_CRUCIBLE = id("rei/crucible/stone")
        val CRUCIBLE_HEAT = id("rei/crucible/heat")

        val CROOK = id("rei/tools/crook")
        val HAMMER = id("rei/tools/hammer")
    }
}