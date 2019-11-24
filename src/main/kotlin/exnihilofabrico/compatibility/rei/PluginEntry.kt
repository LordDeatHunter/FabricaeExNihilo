package exnihilofabrico.compatibility.rei

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.MODID
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.id
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
        // Hackishly Remove the autocrafting button
        helper.registerAutoCraftButtonArea(SIEVE) {_ -> Rectangle(0,0,0,0) }
    }

    override fun registerRecipeDisplays(helper: RecipeHelper) {
        ExNihiloFabrico.LOGGER.info("Registering REI Displays")
        ExNihiloRegistries.SIEVE.getREIRecipes().forEach { helper.registerDisplay(SIEVE, SieveDisplay(it)) }

    }

    companion object {

        val PLUGIN = id("${MODID}_plugin")

        val SIEVE = id("plugins/sieve")

        val ALCHEMY = id("plugins/alchemy")
        val COMPOSTING= id("plugins/composting")
        val LEAKING= id("plugins/leaking")
        val BLEEDING= id("plugins/bleeding")
        val TRANSFORMING= id("plugins/transforming")
    }
}