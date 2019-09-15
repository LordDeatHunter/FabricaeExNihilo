package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder
import com.swordglowsblue.artifice.api.util.Processor
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.api.registry.ExNihiloRegistries.CROOK
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_HEAT
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_STONE
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_WOOD
import exnihilofabrico.api.registry.ExNihiloRegistries.HAMMER
import exnihilofabrico.api.registry.ExNihiloRegistries.MESH
import exnihilofabrico.api.registry.ExNihiloRegistries.ORES
import exnihilofabrico.api.registry.ExNihiloRegistries.SIEVE
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_ENTITY
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_WORLD
import exnihilofabrico.client.FluidRenderManager
import exnihilofabrico.client.ExNihiloItemColorProvider
import exnihilofabrico.client.renderers.CrucibleBlockEntityRenderer
import exnihilofabrico.client.renderers.SieveBlockEntityRenderer
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.common.ModFluids
import exnihilofabrico.common.ModItems
import exnihilofabrico.common.ModTools
import exnihilofabrico.common.crucibles.CrucibleBlockEntity
import exnihilofabrico.common.sieves.SieveBlockEntity
import exnihilofabrico.modules.MetaModule
import exnihilofabrico.util.ExNihiloItemStack
import io.github.cottonmc.cotton.logging.ModLogger
import net.fabricmc.api.*
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

const val MODID: String = "exnihilofabrico"
const val VERSION: String = "0.0a"

// Small helper functions
fun id(name: String) = Identifier(MODID, name)

@EnvironmentInterface(itf=ClientModInitializer::class, value=EnvType.CLIENT)
object ExNihiloFabrico: ModInitializer, ClientModInitializer {
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
    }

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        // Fluid Rendering
        FluidRenderManager.setupClient()
        // Register "BE"SRs
        BlockEntityRendererRegistry.INSTANCE.register(SieveBlockEntity::class.java, SieveBlockEntityRenderer())
        BlockEntityRendererRegistry.INSTANCE.register(CrucibleBlockEntity::class.java, CrucibleBlockEntityRenderer())
        LOGGER.info("Registered BESR for Sieve")

        // Item Colors
        ExNihiloRegistries.ORES.getAll().forEach {
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.getChunkItem())
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.getPieceItem())
        }
        ExNihiloRegistries.MESH.getAll().forEach {
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.item)
        }
        LOGGER.info("Registered ItemColorProviders")

        // Artifice Resource Pack
        val resourcePack: ArtificeResourcePack = Artifice.registerAssets(MODID) { pack ->
            pack.setDisplayName("Ex Nihilo Fabrico Generated Resources")
            pack.setDescription("Generated Resources for Ex Nihilo Fabrico")

            // Barrel models
            ModBlocks.BARRELS.forEach { (k, v) ->
                pack.addBlockModel(k) {model ->
                    model.parent(id("block/barrel"))
                        .texture("all", v.texture)
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }
            // Crucible models
            ModBlocks.CRUCIBLES.forEach { (k, v) ->
                pack.addBlockModel(k) {model ->
                    model.parent(id("block/crucible"))
                        .texture("all", v.texture)
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }
            // Sieve models
            ModBlocks.SIEVES.forEach { (k, v) ->
                pack.addBlockModel(k) {model ->
                    model.parent(id("block/sieve"))
                        .texture("all", v.texture)
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }

            // Mesh models
            for(mesh in ExNihiloRegistries.MESH.getAll()) {
                pack.addItemModel(mesh.identifier) { model ->
                    model.parent(id("item/mesh"))
                }
            }

            // Ore Chunks/Pieces
            for(ore in ExNihiloRegistries.ORES.getAll()) {
                // Ore Chunk Model
                pack.addItemModel(ore.getChunkID()) { model: ModelBuilder ->
                    val chunkShape = ore.chunk.name.toLowerCase()
                    val material = ore.matrix.name.toLowerCase()
                    model
                        .parent(Identifier("item/generated"))
                        .texture("layer0", id("item/ore/chunks/${chunkShape}_${material}"))
                        .texture("layer1", id("item/ore/chunks/${chunkShape}_overlay"))
                }
                // Ore Piece Model
                pack.addItemModel(ore.getPieceID(), Processor { model: ModelBuilder ->
                    val pieceShape = ore.piece.name.toLowerCase()
                    val material = ore.matrix.name.toLowerCase()
                    model
                        .parent(Identifier("item/generated"))
                        .texture("layer0", id("item/ore/pieces/${pieceShape}_${material}"))
                        .texture("layer1", id("item/ore/pieces/${pieceShape}_overlay"))
                })
            }
        }
        LOGGER.info("Created Resources")
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
        MetaModule.registerWitchWaterEntity(WITCHWATER_ENTITY)
        MetaModule.registerWitchWaterFluid(WITCHWATER_WORLD)
        // Misc. Files
    }
}