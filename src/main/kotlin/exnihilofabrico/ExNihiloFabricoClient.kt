package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.client.ExNihiloItemColorProvider
import exnihilofabrico.client.FluidRenderManager
import exnihilofabrico.client.renderers.CrucibleBlockEntityRenderer
import exnihilofabrico.client.renderers.SieveBlockEntityRenderer
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.common.crucibles.CrucibleBlockEntity
import exnihilofabrico.common.sieves.SieveBlockEntity
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.EnvironmentInterface
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.util.Identifier

@EnvironmentInterface(itf= ClientModInitializer::class, value= EnvType.CLIENT)
object ExNihiloFabricoClient: ClientModInitializer {
    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        // Fluid Rendering
        FluidRenderManager.setupClient()
        // Register "BE"SRs
        BlockEntityRendererRegistry.INSTANCE.register(SieveBlockEntity::class.java, SieveBlockEntityRenderer())
        BlockEntityRendererRegistry.INSTANCE.register(CrucibleBlockEntity::class.java, CrucibleBlockEntityRenderer())
        ExNihiloFabrico.LOGGER.info("Registered BESR for Sieve")

        // Item Colors
        ExNihiloRegistries.ORES.getAll().forEach {
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.getChunkItem())
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.getPieceItem())
        }
        ExNihiloRegistries.MESH.getAll().forEach {
            ColorProviderRegistry.ITEM.register(ExNihiloItemColorProvider, it.item)
        }
        ExNihiloFabrico.LOGGER.info("Registered ItemColorProviders")

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
                pack.addItemModel(ore.getPieceID()) { model: ModelBuilder ->
                    val pieceShape = ore.piece.name.toLowerCase()
                    val material = ore.matrix.name.toLowerCase()
                    model
                        .parent(Identifier("item/generated"))
                        .texture("layer0", id("item/ore/pieces/${pieceShape}_${material}"))
                        .texture("layer1", id("item/ore/pieces/${pieceShape}_overlay"))
                }
            }
        }
        ExNihiloFabrico.LOGGER.info("Created Resources")
    }
}