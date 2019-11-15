package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import exnihilofabrico.api.ExNihiloFabricoAPI
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.compatibility.modules.ExNihiloFabrico
import exnihilofabrico.modules.*
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
            builder.setDescription("Crafting recipes")
            LOGGER.info("Creating Tags")
            generateTags(builder)
            LOGGER.info("Creating Recipes")
            generateRecipes(builder)
        }
    }

    private fun generateRecipes(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        ExNihiloFabricoAPI.registerCompatabilityModule(ExNihiloFabrico)
        // Ore Chunk Crafting
        ExNihiloRegistries.ORES.getAll().forEach { ore ->
            builder.addShapedRecipe(id("${ore.getChunkID().path}_crafting")) { ore.generateRecipe(it) }
            if(Registry.ITEM.containsId(ore.getNuggetID())) {
                builder.addSmeltingRecipe(id("${ore.getPieceID().path}_smelting")) { ore.generateNuggetCookingRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getPieceID().path}_blasting")) { ore.generateNuggetCookingRecipe(it) }
            }
            if(Registry.ITEM.containsId(ore.getIngotID())) {
                builder.addSmeltingRecipe(id("${ore.getChunkID().path}_smelting")) { ore.generateIngotCookingRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getChunkID().path}_blasting")) { ore.generateIngotCookingRecipe(it) }
            }
        }
        // Mesh Crafting
        ExNihiloRegistries.MESH.getAll().forEach { mesh -> builder.addShapedRecipe(mesh.identifier) { mesh.generateRecipe(it) } }
        // Mesh Crafting
        ModBlocks.SIEVES.forEach { (k, sieve) -> builder.addShapedRecipe(k) { sieve.generateRecipe(it) } }
        // Crucible Crafting
        ModBlocks.CRUCIBLES.forEach { (k, crucible) -> builder.addShapedRecipe(k) { crucible.generateRecipe(it) } }
        // Barrel Crafting
        ModBlocks.BARRELS.forEach { (k, barrel) -> builder.addShapedRecipe(k) { barrel.generateRecipe(it) } }
    }
    private fun generateTags(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // exnihilofabrico:infested_leaves tag
        (ModTags.INFESTED_LEAVES_BLOCK)?.let {
            builder.addBlockTag(it.id) {tag ->
                tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
            }
            builder.addItemTag(it.id) {tag ->
                tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
            }
        }
        (ModTags.HAMMER_TAG)?.let {
            builder.addBlockTag(it.id) {tag ->
                tag.values(*ModTools.HAMMERS.keys.toTypedArray())
            }
            builder.addItemTag(it.id) {tag ->
                tag.values(*ModTools.HAMMERS.keys.toTypedArray())
            }
        }
        (ModTags.CROOK_TAG)?.let {
            builder.addBlockTag(it.id) {tag ->
                tag.values(*ModTools.CROOKS.keys.toTypedArray())
            }
            builder.addItemTag(it.id) {tag ->
                tag.values(*ModTools.CROOKS.keys.toTypedArray())
            }
        }
        ExNihiloRegistries.ORES.getAll().forEach { property ->
            builder.addItemTag(property.getOreID()) {tag ->
                tag.value(property.getChunkID())
            }
        }
    }
}