package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
import exnihilofabrico.api.ExNihiloFabricoAPI
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.compatibility.modules.ExNihiloFabrico
import exnihilofabrico.modules.*
import exnihilofabrico.util.ArtificeUtils
import exnihilofabrico.util.BlockGenerator
import exnihilofabrico.util.getExNihiloItemStack
import io.github.cottonmc.cotton.config.ConfigManager
import io.github.cottonmc.cotton.logging.ModLogger
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


const val MODID: String = "exnihilofabrico"

// Small helper functions
fun id(name: String) = Identifier(MODID, name)

object ExNihiloFabrico: ModInitializer {
    val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.build(Identifier(MODID, "general")) { getExNihiloItemStack("crook_wood") }
    val LOGGER = ModLogger(MODID)
    val config: ExNihiloFabricoConfig = ConfigManager.loadConfig(ExNihiloFabricoConfig::class.java)

    override fun onInitialize() {
        registerCompatModules()
        // Progmatically generate blocks and items
        LOGGER.info("Generating Blocks/Items")
        BlockGenerator.initRegistryCallBack()

        // Load the early registries that create items/blocks
        ExNihiloRegistries.loadEarlyRegistries()

        /* Register Status Effects */
        LOGGER.info("Registering Status Effects")
        ModEffects.registerEffects(Registry.STATUS_EFFECT)
        /* Register Fluids*/
        LOGGER.info("Registering Fluids")
        ModFluids.registerFluids(Registry.FLUID)
        /* Register Blocks */
        LOGGER.info("Registering Blocks")
        ModBlocks.registerBlocks(Registry.BLOCK)
        /* Register Items */
        LOGGER.info("Registering Items")
        ModBlocks.registerBlockItems(Registry.ITEM)
        ModItems.registerItems(Registry.ITEM)
        ModTools.registerItems(Registry.ITEM)

        /* Register Block Entities */
        LOGGER.info("Registering Block Entities")
        ModBlocks.registerBlockEntities(Registry.BLOCK_ENTITY)

        /* Load the rest of the Ex Nihilo Fabrico registries */
        LOGGER.info("Loading Ex Nihilo Fabrico Registries")
        ExNihiloRegistries.loadRecipeRegistries()

        val dataPack = Artifice.registerData(id("data")) {builder ->
            builder.setDisplayName("Ex Nihilo Fabrico")
            builder.setDescription("Generated datapack for dynamic content")
            LOGGER.info("Creating Tags")
            ArtificeUtils.generateTags(builder)
            LOGGER.info("Creating Recipes")
            ArtificeUtils.generateRecipes(builder)
            LOGGER.info("Creating Loot Tables")
            ArtificeUtils.generateLootTables(builder)
        }
        dataPack.dumpResources("exnihilofabrico_generated")
    }

    private fun registerCompatModules() {
        ExNihiloFabricoAPI.registerCompatabilityModule(ExNihiloFabrico)
    }
}