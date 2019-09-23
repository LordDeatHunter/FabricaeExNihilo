package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.api.registry.ExNihiloRegistries.CROOK
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_HEAT
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_STONE
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_WOOD
import exnihilofabrico.api.registry.ExNihiloRegistries.HAMMER
import exnihilofabrico.api.registry.ExNihiloRegistries.MESH
import exnihilofabrico.api.registry.ExNihiloRegistries.ORES
import exnihilofabrico.api.registry.ExNihiloRegistries.SIEVE
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_WORLD
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.ModFluids
import exnihilofabrico.modules.ModItems
import exnihilofabrico.modules.ModTools
import exnihilofabrico.registry.compat.MetaCompat
import exnihilofabrico.util.BlockGenerator
import exnihilofabrico.util.ExNihiloItemStack
import io.github.cottonmc.cotton.config.ConfigManager
import io.github.cottonmc.cotton.logging.ModLogger
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


const val MODID: String = "exnihilofabrico"
const val VERSION: String = "0.0a"

// Small helper functions
fun id(name: String) = Identifier(MODID, name)

object ExNihiloFabrico: ModInitializer {
    val ITEM_GROUP = FabricItemGroupBuilder.build(Identifier(MODID, "general")) { ExNihiloItemStack("crook_wood") }
    val LOGGER = ModLogger(MODID)
    val config = ConfigManager.loadConfig(ExNihiloFabricoConfig::class.java)

    override fun onInitialize() {
        // Progmatically generate blocks and items
        LOGGER.info("Generating Blocks/Items")
        BlockGenerator.initRegistryCallBack()
        MetaCompat.registerOres(ORES)
        MetaCompat.registerMesh(MESH)

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
        LOGGER.info("Creating Recipes")
        val dataPack = Artifice.registerData(id(MODID)) {builder ->
            generateTags(builder)
            generateRecipes(builder)
        }
        LOGGER.info("Loading Ex Nihilo Fabrico Registries")
        loadRegistries()
    }

    private fun loadRegistries() {
        // Barrel Recipes
        // MetaModule.registerBarrelAlchemy(BARREL_ALCHEMY)
        // MetaModule.registerBarrelMilking(BARREL_MILKING)
        // Crucible Recipes
        MetaCompat.registerCrucibleHeat(CRUCIBLE_HEAT)
        MetaCompat.registerCrucibleStone(CRUCIBLE_STONE)
        MetaCompat.registerCrucibleWood(CRUCIBLE_WOOD)
        // Sieve Recipes
        MetaCompat.registerSieve(SIEVE)
        // Tool Recipes
        MetaCompat.registerHammer(HAMMER)
        MetaCompat.registerCrook(CROOK)
        // Witch Water Recipes
        MetaCompat.registerWitchWaterFluid(WITCHWATER_WORLD)
    }

    private fun generateRecipes(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // Ore Chunk Crafting
        ExNihiloRegistries.ORES.getAll().forEach { ore ->
            builder.addShapedRecipe(ore.getChunkID()) { ore.generateRecipe(it) }
            builder.addBlastingRecipe(id("${ore.getPieceID().path}_blasting")) { ore.generateNuggetCookingRecipe(it) }
            builder.addBlastingRecipe(id("${ore.getChunkID().path}_blasting")) { ore.generateIngotCookingRecipe(it) }
            builder.addSmeltingRecipe(id("${ore.getPieceID().path}_smelting")) { ore.generateNuggetCookingRecipe(it) }
            builder.addSmeltingRecipe(id("${ore.getChunkID().path}_smelting")) { ore.generateIngotCookingRecipe(it) } }
        // Mesh Crafting
        ExNihiloRegistries.MESH.getAll().forEach { mesh -> builder.addShapedRecipe(mesh.identifier) { mesh.generateRecipe(it) } }
        // Mesh Crafting
        ModBlocks.SIEVES.forEach { k, sieve -> builder.addShapedRecipe(k) { sieve.generateRecipe(it) } }
        // Crucible Crafting
        ModBlocks.CRUCIBLES.forEach { k, crucible -> builder.addShapedRecipe(k) { crucible.generateRecipe(it) } }
        // Barrel Crafting
        ModBlocks.BARRELS.forEach { k, barrel -> builder.addShapedRecipe(k) { barrel.generateRecipe(it) } }
    }
    private fun generateTags(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // exnihilofabrico:infested_leaves tag
        builder.addBlockTag(id("infested_leaves")) {tag ->
            tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
        }
        builder.addItemTag(id("infested_leaves")) {tag ->
            tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
        }
        ExNihiloRegistries.ORES.getAll().forEach { property ->
            builder.addItemTag(property.getOreID()) {tag ->
                tag.value(property.getChunkID())
            }
        }
    }
}