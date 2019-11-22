package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.client.ExNihiloBlockColorProvider
import exnihilofabrico.client.ExNihiloItemColorProvider
import exnihilofabrico.client.FluidRenderManager
import exnihilofabrico.client.renderers.BarrelBlockEntityRenderer
import exnihilofabrico.client.renderers.CrucibleBlockEntityRenderer
import exnihilofabrico.client.renderers.InfestingLeavesBlockEntityRenderer
import exnihilofabrico.client.renderers.SieveBlockEntityRenderer
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.BarrelBlockEntity
import exnihilofabrico.modules.crucibles.CrucibleBlockEntity
import exnihilofabrico.modules.infested.InfestingLeavesBlockEntity
import exnihilofabrico.modules.sieves.SieveBlockEntity
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.EnvironmentInterface
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

@EnvironmentInterface(itf= ClientModInitializer::class, value= EnvType.CLIENT)
object ExNihiloFabricoClient: ClientModInitializer {
    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {

        // Fluid Rendering
        FluidRenderManager.setupClient()
        // Register "BE"SRs
        BlockEntityRendererRegistry.INSTANCE.register(SieveBlockEntity::class.java, SieveBlockEntityRenderer())
        BlockEntityRendererRegistry.INSTANCE.register(CrucibleBlockEntity::class.java, CrucibleBlockEntityRenderer())
        BlockEntityRendererRegistry.INSTANCE.register(BarrelBlockEntity::class.java, BarrelBlockEntityRenderer())
        BlockEntityRendererRegistry.INSTANCE.register(InfestingLeavesBlockEntity::class.java, InfestingLeavesBlockEntityRenderer())
        ExNihiloFabrico.LOGGER.info("Registered BESR for Sieve")

        // Item Colors
        ExNihiloRegistries.ORES.getAll().forEach {
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.getChunkItem())
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.getPieceItem())
        }
        ExNihiloRegistries.MESH.getAll().forEach {
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.item)
        }
        ModBlocks.INFESTED_LEAVES.forEach { (_, v) ->
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, v.asItem())
            ColorProviderRegistry.BLOCK.register(ExNihiloBlockColorProvider, v)
        }
        ExNihiloFabrico.LOGGER.info("Registered ItemColorProviders and BlockColorProviders")

        // Artifice Resource Pack
        val resourcePack: ArtificeResourcePack = Artifice.registerAssets(id("resources")) { pack ->
            pack.setDisplayName("Ex Nihilo Fabrico Generated Resources")
            pack.setDescription("Generated Resources for Ex Nihilo Fabrico")

            // Barrel models
            ModBlocks.BARRELS.forEach { (k, v) ->
                pack.addBlockModel(k) {model ->
                    model.parent(id("block/barrel"))
                        .texture("all", v.texture)
                }
                pack.addBlockState(k) {state ->
                    state.variant("") {it.model(id("block/${k.path}")) }
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }
            // Crucible models
            ModBlocks.CRUCIBLES.forEach { (k, v) ->
                pack.addBlockModel(k) {model ->
                    model.parent(id("block/crucible"))
                        .texture("all", v.texture)
                }
                pack.addBlockState(k) {state ->
                    state.variant("") {it.model(id("block/${k.path}")) }
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }
            // Sieve models
            ModBlocks.SIEVES.forEach { (k, v) ->
                pack.addBlockModel(k) {model ->
                    model.parent(id("block/sieve"))
                        .texture("all", v.texture)
                }
                pack.addBlockState(k) {state ->
                    state.variant("") {it.model(id("block/${k.path}")) }
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }

            // Mesh models
            for(mesh in ExNihiloRegistries.MESH.getAll()) {
                pack.addItemModel(mesh.identifier) { model ->
                    model.parent(id("item/mesh"))
                }
            }

            // Infested Leaves models
            ModBlocks.INFESTED_LEAVES.forEach { (k, leaves) ->
                pack.addBlockModel(k) {model ->
                    model.parent(Identifier("item/"+ Registry.BLOCK.getId(leaves.leafBlock).path))
                }
                pack.addBlockState(k) {state ->
                    state.variant("") {it.model(id("block/${k.path}")) }
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }

            // Ore Chunks/Pieces
            for(ore in ExNihiloRegistries.ORES.getAll()) {
                // Ore Chunk Model
                pack.addItemModel(ore.getChunkID()) { model: ModelBuilder ->
                    val chunkShape = ore.chunk.name.toLowerCase()
                    val material = ore.matrix.name.toLowerCase()
                    model
                        .parent(Identifier("item/generated"))
                        .texture("layer0", id("item/ore/chunks/${chunkShape}_$material"))
                        .texture("layer1", id("item/ore/chunks/${chunkShape}_overlay"))
                }
                // Ore Piece Model
                pack.addItemModel(ore.getPieceID()) { model: ModelBuilder ->
                    val pieceShape = ore.piece.name.toLowerCase()
                    val material = ore.matrix.name.toLowerCase()
                    model
                        .parent(Identifier("item/generated"))
                        .texture("layer0", id("item/ore/pieces/${pieceShape}_$material"))
                        .texture("layer1", id("item/ore/pieces/${pieceShape}_overlay"))
                }
            }
        }
        ExNihiloFabrico.LOGGER.info("Created Resources")
        if(ExNihiloFabrico.config.modules.general.dumpGeneratedResource)
            resourcePack.dumpResources("exnihilofabrico_generated")
    }
}