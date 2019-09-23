package exnihilofabrico.compatibility.rei

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.MODID
import exnihilofabrico.id
import me.shedaniel.rei.api.RecipeHelper
import me.shedaniel.rei.api.plugins.REIPluginV0
import net.fabricmc.loader.api.SemanticVersion
import net.fabricmc.loader.util.version.VersionParsingException

class PluginEntry: REIPluginV0 {
    val PLUGIN_ID = id("${MODID}_plugin")
    override fun getPluginIdentifier() = PLUGIN_ID

    @Throws(VersionParsingException::class)
    override fun getMinimumVersion(): SemanticVersion {
        return SemanticVersion.parse("3.0-pre")
    }

    override fun registerPluginCategories(recipeHelper: RecipeHelper) {
        ExNihiloFabrico.LOGGER.info("Registering REI Categories")
    }

    override fun registerRecipeDisplays(recipeHelper: RecipeHelper) {
        ExNihiloFabrico.LOGGER.info("Registering REI Displays")
    }
}