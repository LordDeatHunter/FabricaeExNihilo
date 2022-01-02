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
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
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
import java.util.ArrayList;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

@Environment(EnvType.CLIENT)
public class FabricaeExNihiloClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Fluid Rendering
        FluidRenderManager.setupClient();
        // Register BERs
        BlockEntityRendererRegistry.register(SieveBlockEntity.TYPE, SieveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(CrucibleBlockEntity.TYPE, CrucibleBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BarrelBlockEntity.TYPE, BarrelBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(InfestingLeavesBlockEntity.TYPE, InfestingLeavesBlockEntityRenderer::new);
        FabricaeExNihilo.LOGGER.debug("Registered BERs");

        // Color Providers
        var coloredItems = new ArrayList<ItemConvertible>();
    
        FabricaeExNihilo.MESHES.keySet().stream().map(Registry.ITEM::get).forEach(coloredItems::add);
        FabricaeExNihilo.ORES.keySet().stream().map(id1 -> id1 + "_chunk").map(FabricaeExNihilo::id).toList().stream().map(Registry.ITEM::get).forEach(coloredItems::add);
        FabricaeExNihilo.ORES.keySet().stream().map(id1 -> id1 + "_piece").map(FabricaeExNihilo::id).toList().stream().map(Registry.ITEM::get).forEach(coloredItems::add);
        coloredItems.addAll(ModBlocks.INFESTED_LEAVES.values());
        
        ColorProviderRegistry.ITEM.register(FabricaeExNihiloItemColorProvider.INSTANCE, coloredItems.toArray(ItemConvertible[]::new));
        
        ColorProviderRegistry.BLOCK.register(FabricaeExNihiloBlockColorProvider.INSTANCE, ModBlocks.INFESTED_LEAVES.values().toArray(Block[]::new));
        FabricaeExNihilo.LOGGER.debug("Registered color providers");
        
        // RenderLAyers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.SIEVES.values().toArray(Block[]::new));
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.CRUCIBLES.values().toArray(Block[]::new));

        // ARRP resource pack
        var resourcePack = RuntimeResourcePack.create(id("resources"));
        // Mesh models
        for (var mesh : FabricaeExNihilo.MESHES.keySet()) {
            resourcePack.addModel(JModel.modelKeepElements(id("item/mesh")), id("item/" + mesh.getPath()));
        }
        // Infested Leaves models
        ModBlocks.INFESTED_LEAVES.forEach((identifier, infestedLeaves) -> {
            var root = Registry.BLOCK.getId(infestedLeaves.getLeafBlock());
            resourcePack.addModel(JModel.modelKeepElements(root.getNamespace() + ":block/" + root.getPath()), id("block/" + identifier.getPath()));
            resourcePack.addModel(JModel.modelKeepElements(id("block/" + identifier.getPath())), id("item/" + identifier.getPath()));
            resourcePack.addBlockState(JState.state(JState.variant(JState.model(id("block/" + identifier.getPath())))), identifier);
        });
        // Ore Chunks/Pieces
        // Ore Chunk Model
        // Ore Piece Model
        FabricaeExNihilo.ORES.forEach((id, ore) -> {
            var chunkShape = ore.chunkShape().name().toLowerCase();
            var material = ore.chunkMaterial().name().toLowerCase();
            resourcePack.addModel(
                    JModel.modelKeepElements("item/generated")
                            .textures(
                                    JModel.textures()
                                            .layer0(id("item/ore/chunks/" + chunkShape + "_" + material).toString())
                                            .layer1(id("item/ore/chunks/" + chunkShape + "_overlay").toString())),
                    id("item/" + id + "_chunk"));
            var pieceShape = ore.pieceShape().name().toLowerCase();
            resourcePack.addModel(
                    JModel.modelKeepElements("item/generated")
                            .textures(
                                    JModel.textures()
                                            .layer0(id("item/ore/pieces/" + pieceShape + "_" + material).toString())
                                            .layer1(id("item/ore/pieces/" + pieceShape + "_overlay").toString())),
                    id("item/" + id + "_piece"));
        });

        FabricaeExNihilo.LOGGER.debug("Created Resources");
        if (FabricaeExNihilo.CONFIG.dumpGeneratedResource) {
            resourcePack.dump(Path.of("fabricaeexnihilo_generated"));
        }

        RRPCallback.BEFORE_VANILLA.register(a -> a.add(resourcePack));
    }

}
