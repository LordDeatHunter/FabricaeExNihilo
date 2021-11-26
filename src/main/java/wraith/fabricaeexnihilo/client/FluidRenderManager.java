package wraith.fabricaeexnihilo.client;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;

import java.util.function.Function;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
import static net.minecraft.util.registry.Registry.FLUID;

public class FluidRenderManager implements ClientSpriteRegistryCallback {

    @Override
    public void registerSprites(SpriteAtlasTexture atlasTexture, ClientSpriteRegistryCallback.Registry registry) {
        ModFluids.FLUIDS.forEach(fluid -> {
            registry.register(fluid.getFluidSettings().getFlowingTexture());
            registry.register(fluid.getFluidSettings().getStillTexture());
        });
    }

    public static void setupClient() {
        var renderManager = new FluidRenderManager();
        ClientSpriteRegistryCallback.event(BLOCK_ATLAS_TEXTURE).register(renderManager);
        ModFluids.FLUIDS.forEach(FluidRenderManager::setupFluidRenderer);
    }

    private static void setupFluidRenderer(AbstractFluid fluid) {
        final Identifier fluidId = FLUID.getId(fluid);
        final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

        final Sprite[] fluidSprites = {null, null};

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
    }

}
