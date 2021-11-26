package wraith.fabricaeexnihilo.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.client.renderers.BarrelBlockEntityRenderer;
import wraith.fabricaeexnihilo.client.renderers.CrucibleBlockEntityRenderer;
import wraith.fabricaeexnihilo.client.renderers.InfestingLeavesBlockEntityRenderer;
import wraith.fabricaeexnihilo.client.renderers.SieveBlockEntityRenderer;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;

@Environment(EnvType.CLIENT)
public class FabricaeExNihiloClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Fluid Rendering
        FluidRenderManager.setupClient();
        // Register "BE"SRs
        BlockEntityRendererRegistry.register(SieveBlockEntity.TYPE, SieveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(CrucibleBlockEntity.TYPE, CrucibleBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BarrelBlockEntity.TYPE, BarrelBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(InfestingLeavesBlockEntity.TYPE, InfestingLeavesBlockEntityRenderer::new);
        FabricaeExNihilo.LOGGER.info("Registered BESR for Sieve");

        // Item Colors
        FabricaeExNihiloRegistries.ORES.getAll().forEach(ore -> {
            ColorProviderRegistry.ITEM.register(FabricaeExNihiloItemColorProvider.INSTANCE, ore.getChunkItem());
            ColorProviderRegistry.ITEM.register(FabricaeExNihiloItemColorProvider.INSTANCE, ore.getPieceItem());
        });
        FabricaeExNihiloRegistries.MESH.getAll().forEach(mesh -> ColorProviderRegistry.ITEM.register(FabricaeExNihiloItemColorProvider.INSTANCE, mesh.getItem()));
        ModBlocks.INFESTED_LEAVES.forEach((identifier, leaves) -> {
            ColorProviderRegistry.ITEM.register(FabricaeExNihiloItemColorProvider.INSTANCE, leaves.asItem());
            ColorProviderRegistry.BLOCK.register(FabricaeExNihiloBlockColorProvider.INSTANCE, leaves);
        });
        FabricaeExNihilo.LOGGER.info("Registered ItemColorProviders and BlockColorProviders");
        ModBlocks.SIEVES.forEach((identifier, sieve) -> BlockRenderLayerMap.INSTANCE.putBlock(sieve, RenderLayer.getCutout()));
        ModBlocks.CRUCIBLES.forEach((identifier, crucible) -> BlockRenderLayerMap.INSTANCE.putBlock(crucible, RenderLayer.getCutout()));

        /*
        TODO
        // ARRP resource pack
        val resourcePack: ArtificeResourcePack = Artifice.registerAssets(ID("resources")) { pack ->
                pack.setDisplayName("Fabricae Ex Nihilo Generated Resources")
            pack.setDescription("Generated Resources for Fabricae Ex Nihilo")

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
            for(mesh in FabricaeExNihiloRegistries.MESH.getAll()) {
                pack.addItemModel(mesh.identifier) { model ->
                        model.parent(id("item/mesh"))
                }
            }

            // Infested Leaves models
            ModBlocks.INFESTED_LEAVES.forEach { (k, leaves) ->
                    pack.addBlockModel(k) {model ->
                    val root = Registry.BLOCK.getId(leaves.leafBlock)
                model.parent(Identifier(root.namespace, "block/${root.path}"))
            }
                pack.addBlockState(k) {state ->
                        state.variant("") {it.model(id("block/${k.path}")) }
                }
                pack.addItemModel(k) { model -> model.parent(id("block/${k.path}")) }
            }

            // Ore Chunks/Pieces
            for(ore in FabricaeExNihiloRegistries.ORES.getAll()) {
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
        FabricaeExNihilo.LOGGER.info("Created Resources");
        if(FabricaeExNihilo.CONFIG.dumpGeneratedResource) {
            resourcePack.dumpResources("fabricaeexnihilo_generated");
        }
         */
    }

}
