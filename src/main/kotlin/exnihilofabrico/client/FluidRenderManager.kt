package exnihilofabrico.client

import exnihilofabrico.modules.base.AbstractFluid
import exnihilofabrico.modules.fluids.MilkFluid
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.SpriteAtlasTexture

class FluidRenderManager: ClientSpriteRegistryCallback {
    override fun registerSprites(atlas: SpriteAtlasTexture?, registry: ClientSpriteRegistryCallback.Registry?) {
        registry?.register(WitchWaterFluid.fluidSettings.flowingTexture)
        registry?.register(WitchWaterFluid.fluidSettings.stillTexture)
        registry?.register(MilkFluid.fluidSettings.flowingTexture)
        registry?.register(MilkFluid.fluidSettings.stillTexture)
    }
    companion object {
        fun setupClient() {
            val renderManager = FluidRenderManager()
            ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register(renderManager)
            setupFluidRenderer(WitchWaterFluid.Still)
            setupFluidRenderer(WitchWaterFluid.Flowing)
            setupFluidRenderer(MilkFluid.Still)
            setupFluidRenderer(MilkFluid.Flowing)
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