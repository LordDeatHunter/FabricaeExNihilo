package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
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
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.common.ModFluids
import exnihilofabrico.common.ModItems
import exnihilofabrico.common.ModTools
import exnihilofabrico.modules.MetaModule
import exnihilofabrico.util.ExNihiloItemStack
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
//    val LOGGER = ModLogger(MODID, "ExNihiloFabrico")
    val LOGGER = ModLogger(MODID)

    override fun onInitialize() {
        loadConfigs()

        /* OreRegistry and MeshRegistry is used to create items so must be first */
        MetaModule.registerOres(ORES)
        MetaModule.registerMesh(MESH)
        /* Register Fluids*/
        LOGGER.info("Registering Fluids")
        ModFluids.registerFluids(Registry.FLUID)
        LOGGER.info("Registered Fluids")
        /* Register Blocks */
        LOGGER.info("Registering Blocks")
        ModBlocks.registerBlocks(Registry.BLOCK)
        LOGGER.info("Registered Blocks")
        /* Register Items */
        LOGGER.info("Registering Items")
        ModBlocks.registerBlockItems(Registry.ITEM)
        ModItems.registerItems(Registry.ITEM)
        ModTools.registerItems(Registry.ITEM)
        LOGGER.info("Registered Items")

        /* Register Block Entities */
        LOGGER.info("Registering Block Entities")
        ModBlocks.registerBlockEntities(Registry.BLOCK_ENTITY)
        LOGGER.info("Registered Block Entities")

        /* Load the rest of the Ex Nihilo Fabrico registries */
        ORES.registerCraftingRecipes()
        loadRegistries()
        generateRecipes()
    }

    private fun loadConfigs() {

    }

    private fun loadRegistries() {
        // Barrel Recipes
        // MetaModule.registerBarrelAlchemy(BARREL_ALCHEMY)
        // MetaModule.registerBarrelMilking(BARREL_MILKING)
        // Crucible Recipes
        MetaModule.registerCrucibleHeat(CRUCIBLE_HEAT)
        MetaModule.registerCrucibleStone(CRUCIBLE_STONE)
        MetaModule.registerCrucibleWood(CRUCIBLE_WOOD)
        // Sieve Recipes
        MetaModule.registerSieve(SIEVE)
        // Tool Recipes
        MetaModule.registerHammer(HAMMER)
        MetaModule.registerCrook(CROOK)
        // Witch Water Recipes
        MetaModule.registerWitchWaterFluid(WITCHWATER_WORLD)
        // Misc. Files
    }

    private fun generateRecipes() {
        val dataPack = Artifice.registerData(MODID) {builder ->
            // Ore Chunk Crafting
            ExNihiloRegistries.ORES.getAll().forEach { ore ->
                builder.addShapedRecipe(ore.getChunkID()) { ore.generateRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getPieceID().path}_blasting")) { ore.generateNuggetCookingRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getChunkID().path}_blasting")) { ore.generateIngotCookingRecipe(it) }
                builder.addSmeltingRecipe(ore.getPieceID()) { ore.generateNuggetCookingRecipe(it) }
                builder.addSmeltingRecipe(ore.getChunkID()) { ore.generateIngotCookingRecipe(it) } }

            // Mesh Crafting
            ExNihiloRegistries.MESH.getAll().forEach { mesh ->
                builder.addShapedRecipe(mesh.identifier) { mesh.generateRecipe(it) } }

            // Mesh Crafting
            ModBlocks.SIEVES.forEach { k, sieve ->
                builder.addShapedRecipe(k) { sieve.generateRecipe(it) } }

            // Crucible Crafting
            ModBlocks.CRUCIBLES.forEach { k, crucible ->
                builder.addShapedRecipe(k) { crucible.generateRecipe(it) } }

            // Barrel Crafting
            ModBlocks.BARRELS.forEach { k, barrel ->
                builder.addShapedRecipe(k) { barrel.generateRecipe(it) } }
        }
    }
    private fun generateTags() {
        val dataPack = Artifice.registerData(MODID) { builder ->
            // exnihilofabrico:infested_leaves tag
            builder.addBlockTag(id("infested_leaves")) {tag ->
                tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
            }
            builder.addItemTag(id("infested_leaves")) {tag ->
                tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
            }
        }
    }
}