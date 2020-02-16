package exnihilofabrico.client.renderers

import com.mojang.blaze3d.systems.RenderSystem
import exnihilofabrico.modules.infested.InfestingLeavesBlockEntity
import exnihilofabrico.util.asStack
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack


class InfestingLeavesBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<InfestingLeavesBlockEntity>(dispatcher) {
    override fun render(infesting: InfestingLeavesBlockEntity?, partialTicks: Float, matrices: MatrixStack, vertexConsumer: VertexConsumerProvider?, light: Int, overlay: Int) {
        if(infesting == null) return

        val color = infesting.getColor(0) or -16777216
        val stack = infesting.infestedBlock.asItem().asStack()
//        val bakedModel = MinecraftClient.getInstance().itemRenderer.models.getModel(stack)



        // Set GlState
        MinecraftClient.getInstance().textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
        MinecraftClient.getInstance().textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX)?.setFilter(false, false)
        matrices.push()
    //    ModelTransformation.applyGl(bakedModel.transformation.getTransformation(ModelTransformation.Mode.NONE), false)
        RenderSystem.translated(infesting.pos.x.toDouble(), infesting.pos.y.toDouble(), infesting.pos.z.toDouble())
        val r = (color shr 16 and 255).toFloat() / 255.0f
        val g = (color shr 8 and 255).toFloat() / 255.0f
        val b = (color and 255).toFloat() / 255.0f
        RenderSystem.color3f(r, g, b)
        MinecraftClient.getInstance().itemRenderer.renderItem(stack, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumer)

        // Render Quads
//        renderBakedModelColored(bakedModel, color)

        // Clean Up
        RenderSystem.enableCull()
    //    GlStateManager.cullFace(GlStateManager.FaceSides.BACK)
        matrices.pop()
    }
}
