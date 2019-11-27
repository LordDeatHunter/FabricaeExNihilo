package exnihilofabrico.compatibility.rei

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.compatibility.rei.barrel.*
import exnihilofabrico.compatibility.rei.crucible.CrucibleCategory
import exnihilofabrico.compatibility.rei.crucible.CrucibleDisplay
import exnihilofabrico.compatibility.rei.crucible.CrucibleHeatCategory
import exnihilofabrico.compatibility.rei.crucible.CrucibleHeatDisplay
import exnihilofabrico.compatibility.rei.sieve.SieveCategory
import exnihilofabrico.compatibility.rei.sieve.SieveDisplay
import exnihilofabrico.compatibility.rei.tools.ToolCategory
import exnihilofabrico.compatibility.rei.tools.ToolDisplay
import exnihilofabrico.compatibility.rei.witchwater.WitchWaterEntityCategory
import exnihilofabrico.compatibility.rei.witchwater.WitchWaterEntityDisplay
import exnihilofabrico.compatibility.rei.witchwater.WitchWaterWorldCategory
import exnihilofabrico.compatibility.rei.witchwater.WitchWaterWorldDisplay
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

        helper.registerCategory(CrucibleHeatCategory())
        helper.registerCategory(CrucibleCategory(WOOD_CRUCIBLE, getExNihiloItemStack("oak_crucible"), "Wood Crucible"))
        helper.registerCategory(CrucibleCategory(STONE_CRUCIBLE, getExNihiloItemStack("stone_crucible"), "Stone Crucible"))

        helper.registerCategory(CompostCategory())
        helper.registerCategory(LeakingCategory())
        helper.registerCategory(FluidOnTopCategory())
        helper.registerCategory(MilkingCategory())
        helper.registerCategory(TransformingCategory())
        helper.registerCategory(AlchemyCategory())

        helper.registerCategory(WitchWaterEntityCategory())
        helper.registerCategory(WitchWaterWorldCategory())

        // Hackishly Remove the autocrafting button
        helper.registerAutoCraftButtonArea(SIEVE) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(CROOK) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(HAMMER) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(CRUCIBLE_HEAT) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(WOOD_CRUCIBLE) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(STONE_CRUCIBLE) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(COMPOSTING) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(LEAKING) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(ON_TOP) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(MILKING) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(TRANSFORMING) { _ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(WITCH_WATER_ENTITY) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(WITCH_WATER_WORLD) {_ -> Rectangle(0,0,0,0) }
        helper.registerAutoCraftButtonArea(ALCHEMY) { _ -> Rectangle(0,0,0,0) }
    }

    override fun registerRecipeDisplays(helper: RecipeHelper) {
        ExNihiloFabrico.LOGGER.info("Registering REI Displays")
        ExNihiloRegistries.SIEVE.getREIRecipes().forEach { helper.registerDisplay(SIEVE, SieveDisplay(it)) }
        ExNihiloRegistries.CROOK.getREIRecipes().forEach { helper.registerDisplay(CROOK, ToolDisplay(it, CROOK)) }
        ExNihiloRegistries.HAMMER.getREIRecipes().forEach { helper.registerDisplay(HAMMER, ToolDisplay(it, HAMMER)) }
        ExNihiloRegistries.CRUCIBLE_HEAT.getREIRecipes().forEach { helper.registerDisplay(CRUCIBLE_HEAT, CrucibleHeatDisplay(it)) }
        ExNihiloRegistries.CRUCIBLE_WOOD.getREIRecipes().forEach { helper.registerDisplay(WOOD_CRUCIBLE, CrucibleDisplay(it, WOOD_CRUCIBLE)) }
        ExNihiloRegistries.CRUCIBLE_STONE.getREIRecipes().forEach { helper.registerDisplay(STONE_CRUCIBLE, CrucibleDisplay(it, STONE_CRUCIBLE)) }
        ExNihiloRegistries.BARREL_ALCHEMY.getREIRecipes().forEach { helper.registerDisplay(ALCHEMY, AlchemyDisplay(it)) }
        ExNihiloRegistries.BARREL_COMPOST.getREIRecipes().forEach { helper.registerDisplay(COMPOSTING, CompostDisplay(it)) }
        ExNihiloRegistries.BARREL_LEAKING.getREIRecipes().forEach { helper.registerDisplay(LEAKING, LeakingDisplay(it)) }
        ExNihiloRegistries.BARREL_ON_TOP.getREIRecipes().forEach { helper.registerDisplay(ON_TOP, FluidOnTopDisplay(it)) }
        ExNihiloRegistries.BARREL_MILKING.getREIRecipes().forEach { helper.registerDisplay(MILKING, MilkingDisplay(it)) }
        ExNihiloRegistries.BARREL_TRANSFORM.getREIRecipes().forEach { helper.registerDisplay(TRANSFORMING, TransformingDisplay(it)) }
        ExNihiloRegistries.WITCHWATER_ENTITY.getREIRecipes().forEach { helper.registerDisplay(WITCH_WATER_ENTITY, WitchWaterEntityDisplay(it)) }
        ExNihiloRegistries.WITCHWATER_WORLD.getREIRecipes().forEach { helper.registerDisplay(WITCH_WATER_WORLD, WitchWaterWorldDisplay(it)) }

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
        val ON_TOP= id("rei/barrel/fluid_on_top")

        val WOOD_CRUCIBLE = id("rei/crucible/wood")
        val STONE_CRUCIBLE = id("rei/crucible/stone")
        val CRUCIBLE_HEAT = id("rei/crucible/heat")

        val WITCH_WATER_WORLD = id("rei/witchwater/world")
        val WITCH_WATER_ENTITY = id("rei/witchwater/entity")

        val CROOK = id("rei/tools/crook")
        val HAMMER = id("rei/tools/hammer")
    }
}