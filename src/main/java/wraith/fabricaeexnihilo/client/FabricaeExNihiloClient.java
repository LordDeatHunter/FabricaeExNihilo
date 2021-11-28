package wraith.fabricaeexnihilo.client;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.registry.Registry;
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

import java.nio.file.Path;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.ID;

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

        // ARRP resource pack
        var resourcePack = RuntimeResourcePack.create(ID("resources"));
        // Barrel models
        ModBlocks.BARRELS.forEach((identifier, barrel) -> {
            resourcePack.addModel(JModel.modelKeepElements(ID("block/barrel")).textures(JModel.textures().var("all", barrel.getTexture().toString())), ID("block/" + identifier.getPath()));
            resourcePack.addModel(JModel.modelKeepElements(ID("block/" + identifier.getPath())), ID("item/" + identifier.getPath()));
            resourcePack.addBlockState(JState.state(JState.variant(JState.model(ID("block/" + identifier.getPath())))), identifier);
        });
        // Crucible models
        ModBlocks.CRUCIBLES.forEach((identifier, crucible) -> {
            resourcePack.addModel(JModel.modelKeepElements(ID("block/crucible")).textures(JModel.textures().var("all", crucible.getTexture().toString())), ID("block/" + identifier.getPath()));
            resourcePack.addModel(JModel.modelKeepElements(ID("block/" + identifier.getPath())), ID("item/" + identifier.getPath()));
            resourcePack.addBlockState(JState.state(JState.variant(JState.model(ID("block/" + identifier.getPath())))), identifier);
        });
        // Sieve models
        ModBlocks.SIEVES.forEach((identifier, sieve) -> {
            resourcePack.addModel(JModel.modelKeepElements(ID("block/sieve")).textures(JModel.textures().var("all", sieve.getTexture().toString())), ID("block/" + identifier.getPath()));
            resourcePack.addModel(JModel.modelKeepElements(ID("block/" + identifier.getPath())), ID("item/" + identifier.getPath()));
            resourcePack.addBlockState(JState.state(JState.variant(JState.model(ID("block/" + identifier.getPath())))), identifier);
        });
        // Mesh models
        for (var mesh : FabricaeExNihiloRegistries.MESH.getAll()) {
            resourcePack.addModel(JModel.modelKeepElements(ID("item/mesh")), ID("item/" + mesh.getIdentifier().getPath()));
        }
        // Infested Leaves models
        ModBlocks.INFESTED_LEAVES.forEach((identifier, infestedLeaves) -> {
            var root = Registry.BLOCK.getId(infestedLeaves.getLeafBlock());
            resourcePack.addModel(JModel.modelKeepElements(root.getNamespace() + ":block/" + root.getPath()), ID("block/" + identifier.getPath()));
            resourcePack.addModel(JModel.modelKeepElements(ID("block/" + identifier.getPath())), ID("item/" + identifier.getPath()));
            resourcePack.addBlockState(JState.state(JState.variant(JState.model(ID("block/" + identifier.getPath())))), identifier);
        });
        // Ore Chunks/Pieces
        for (var ore : FabricaeExNihiloRegistries.ORES.getAll()) {
            // Ore Chunk Model
            var chunkShape = ore.getChunkShape().name().toLowerCase();
            var material = ore.getChunkMaterial().name().toLowerCase();
            resourcePack.addModel(
                    JModel.modelKeepElements("item/generated")
                            .textures(
                                    JModel.textures()
                                            .layer0(ID("item/ore/chunks/" + chunkShape + "_" + material).toString())
                                            .layer1(ID("item/ore/chunks/" + chunkShape + "_overlay").toString())),
                    ID("item/" + ore.getChunkID().getPath()));
            // Ore Piece Model
            var pieceShape = ore.getPieceShape().name().toLowerCase();
            resourcePack.addModel(
                    JModel.modelKeepElements("item/generated")
                            .textures(
                                    JModel.textures()
                                            .layer0(ID("item/ore/pieces/" + pieceShape + "_" + material).toString())
                                            .layer1(ID("item/ore/pieces/" + pieceShape + "_overlay").toString())),
                    ID("item/" + ore.getPieceID().getPath()));
        }

        FabricaeExNihilo.LOGGER.info("Created Resources");
        if (FabricaeExNihilo.CONFIG.dumpGeneratedResource) {
            resourcePack.dump(Path.of("fabricaeexnihilo_generated"));
        }

        RRPCallback.BEFORE_VANILLA.register(a -> a.add(resourcePack));
    }

}
