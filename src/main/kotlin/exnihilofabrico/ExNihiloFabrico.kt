package exnihilofabrico

import exnihilofabrico.api.registry.ExNihiloRegistries.CROOK
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_HEAT
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_STONE
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_WOOD
import exnihilofabrico.api.registry.ExNihiloRegistries.HAMMER
import exnihilofabrico.api.registry.ExNihiloRegistries.ORES
import exnihilofabrico.api.registry.ExNihiloRegistries.SIEVE
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_ENTITY
import exnihilofabrico.api.registry.ExNihiloRegistries.WITCHWATER_WORLD
import exnihilofabrico.client.FluidRenderManager
import exnihilofabrico.client.OreChunkUnbakedModel
import exnihilofabrico.client.OrePieceUnbakedModel
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
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
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

        /* OreRegistry is used to create items so must be first */
        MetaModule.registerOres(ORES)
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

        // Register Ore Chunk/Piece Renderer
        // Base Models
        ModelLoadingRegistry.INSTANCE.registerAppender { _, out ->
            LOGGER.info("Registering Base Ore Chunk/Piece models")
            out.accept(ModelIdentifier(id("ore_chunk"), "inventory"))
            out.accept(ModelIdentifier(id("ore_piece"), "inventory"))
        }
        ModelLoadingRegistry.INSTANCE.registerVariantProvider {
            object: ModelVariantProvider{
                override fun loadModelVariant(identifier: ModelIdentifier, context: ModelProviderContext): UnbakedModel? {
                    if(identifier.namespace == MODID) {
                        if(identifier.path.contains("_chunk_"))
                            return OreChunkUnbakedModel
                        else if(identifier.path.contains("_piece_"))
                            return OrePieceUnbakedModel
                    }
                    return null
                }
            }
        }
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