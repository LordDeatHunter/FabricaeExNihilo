package exnihilofabrico.client

import exnihilofabrico.modules.ModFluids
import exnihilofabrico.modules.base.AbstractFluid
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.SpriteAtlasTexture

class FluidRenderManager: ClientSpriteRegistryCallback {
    override fun registerSprites(atlas: SpriteAtlasTexture?, registry: ClientSpriteRegistryCallback.Registry?) {
        ModFluids.FLUIDS.forEach {
            registry?.register(it.fluidSettings.flowingTexture)
            registry?.register(it.fluidSettings.stillTexture)
        }
    }
    companion object {
        fun setupClient() {
            val renderManager = FluidRenderManager()
            ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register(renderManager)
            ModFluids.FLUIDS.forEach {
                setupFluidRenderer(it.still as AbstractFluid)
                setupFluidRenderer(it.flowing as AbstractFluid)
            }
        }
        private fun setupFluidRenderer(fluid: AbstractFluid) {
            val sprites = lazy {
                val atlas = MinecraftClient.getInstance().spriteAtlas
                arrayOf(
                    atlas.getSprite(fluid.fluidSettings.stillTexture),
                    atlas.getSprite(fluid.fluidSettings.flowingTexture)
                )
            }

            FluidRenderHandlerRegistry.INSTANCE.register(fluid) { _, _, _ -> sprites.value }
        }
    }
}