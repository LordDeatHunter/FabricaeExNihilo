package wraith.fabricaeexnihilo.client;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
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
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.client.renderers.BarrelBlockEntityRenderer;
import wraith.fabricaeexnihilo.client.renderers.CrucibleBlockEntityRenderer;
import wraith.fabricaeexnihilo.client.renderers.InfestingLeavesBlockEntityRenderer;
import wraith.fabricaeexnihilo.client.renderers.SieveBlockEntityRenderer;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
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
        
        coloredItems.addAll(ModItems.MESHES.values());
        coloredItems.addAll(ModItems.ORE_PIECES.values());
        coloredItems.addAll(ModItems.ORE_CHUNKS.values());
        coloredItems.addAll(ModBlocks.INFESTED_LEAVES.values());
        
        ColorProviderRegistry.ITEM.register(FabricaeExNihiloItemColorProvider.INSTANCE, coloredItems.toArray(ItemConvertible[]::new));
        
        ColorProviderRegistry.BLOCK.register(FabricaeExNihiloBlockColorProvider.INSTANCE, ModBlocks.INFESTED_LEAVES.values().toArray(Block[]::new));
        FabricaeExNihilo.LOGGER.debug("Registered color providers");
        
        // Render Layers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.SIEVES.values().toArray(Block[]::new));
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.CRUCIBLES.values().toArray(Block[]::new));
        
        // TODO: Get rid of this needless arrp code. Every addon can do this themselves
        // ARRP resource pack
        var resourcePack = RuntimeResourcePack.create(id("resources"));
        // Mesh models
        for (var mesh : ModItems.MESHES.keySet()) {
            resourcePack.addModel(JModel.modelKeepElements(id("item/mesh")), id("item/" + mesh.getPath()));
        }
        // Ore Chunks/Pieces
        // Ore Chunk Model
        ModItems.ORE_CHUNKS.forEach((id, item) -> {
            var shape = item.getDefinition().chunkShape().name().toLowerCase();
            var material = item.getDefinition().baseMaterial().name().toLowerCase();
            resourcePack.addModel(
                    JModel.modelKeepElements("item/generated")
                            .textures(
                                    JModel.textures()
                                            .layer0(id("item/ore/chunks/" + shape + "_" + material).toString())
                                            .layer1(id("item/ore/chunks/" + shape + "_overlay").toString())),
                    new Identifier(id.getNamespace(), "item/" + id.getPath()));
        });
        
        // Ore Piece Model
        ModItems.ORE_PIECES.forEach((id, item) -> {
            var shape = item.getDefinition().pieceShape().name().toLowerCase();
            var material = item.getDefinition().baseMaterial().name().toLowerCase();
            resourcePack.addModel(
                    JModel.modelKeepElements("item/generated")
                            .textures(
                                    JModel.textures()
                                            .layer0(id("item/ore/pieces/" + shape + "_" + material).toString())
                                            .layer1(id("item/ore/pieces/" + shape + "_overlay").toString())),
                    new Identifier(id.getNamespace(), "item/" + id.getPath()));
        });
        
        FabricaeExNihilo.LOGGER.debug("Created Resources");
        if (FabricaeExNihilo.CONFIG.dumpGeneratedResource) {
            resourcePack.dump(Path.of("fabricaeexnihilo_generated"));
        }
        
        RRPCallback.BEFORE_VANILLA.register(a -> a.add(resourcePack));
    }
    
}
