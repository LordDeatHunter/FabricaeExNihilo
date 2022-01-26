package wraith.fabricaeexnihilo.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemConvertible;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.client.renderers.*;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlockEntity;

import java.util.ArrayList;

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
        BlockEntityRendererRegistry.register(StrainerBlockEntity.TYPE, StrainerBlockEntityRenderer::new);
        FabricaeExNihilo.LOGGER.debug("Registered BERs");

        // Color Providers
        var coloredItems = new ArrayList<ItemConvertible>();

        coloredItems.addAll(ModItems.MESHES.values());
        coloredItems.addAll(ModItems.ORE_PIECES.values());
        coloredItems.addAll(ModBlocks.INFESTED_LEAVES.values());

        ColorProviderRegistry.ITEM.register(FENItemColorProvider.INSTANCE, coloredItems.toArray(ItemConvertible[]::new));

        ColorProviderRegistry.BLOCK.register(FENBlockColorProvider.INSTANCE, ModBlocks.INFESTED_LEAVES.values().toArray(Block[]::new));
        FabricaeExNihilo.LOGGER.debug("Registered color providers");

        // Render Layers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.SIEVES.values().toArray(Block[]::new));
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.CRUCIBLES.values().toArray(Block[]::new));
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.STRAINERS.values().toArray(Block[]::new));
    }

}
