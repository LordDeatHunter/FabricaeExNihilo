package wraith.fabricaeexnihilo.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;

import java.util.function.Function;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

public class FluidRenderManager {

    public static void setupClient() {
        ModFluids.FLUIDS.forEach(FluidRenderManager::setupFluidRenderer);
    }

    private static void setupFluidRenderer(AbstractFluid fluid) {
        var identifier = Registry.FLUID.getId(fluid);
        final Identifier listenerId = new Identifier(identifier.getNamespace(), identifier.getPath() + "_reload_listener");

        final Sprite[] fluidSprites = {null, null};

        ClientSpriteRegistryCallback.event(BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(fluid.getFluidSettings().getStillTexture());
            registry.register(fluid.getFluidSettings().getFlowingTexture());
        });

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return listenerId;
            }

            @Override
            public void reload(ResourceManager resourceManager) {
                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(BLOCK_ATLAS_TEXTURE);
                fluidSprites[0] = atlas.apply(fluid.getFluidSettings().getStillTexture());
                fluidSprites[1] = atlas.apply(fluid.getFluidSettings().getFlowingTexture());
            }
        });
        FluidRenderHandlerRegistry.INSTANCE.register(fluid, (view, pos, state) -> fluidSprites);
        FluidRenderHandlerRegistry.INSTANCE.register(fluid.getFlowing(), (view, pos, state) -> fluidSprites);

        BlockRenderLayerMap.INSTANCE.putFluid(fluid, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFlowing(), RenderLayer.getCutout());
    }

}
