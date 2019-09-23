package exnihilofabrico.client.renderers

import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.client.renderers.RenderHelper.renderBakedModelColored
import exnihilofabrico.modules.infested.InfestingLeavesBlockEntity
import exnihilofabrico.util.asStack
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.SpriteAtlasTexture


class InfestingLeavesBlockEntityRenderer: BlockEntityRenderer<InfestingLeavesBlockEntity>() {
    override fun render(infesting: InfestingLeavesBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        if(infesting == null) return

        val color = infesting.getColor(0) or -16777216
        val stack = infesting.infestedBlock.asItem().asStack()
        val bakedModel = MinecraftClient.getInstance().itemRenderer.getModel(stack)

        // Set GlState
        MinecraftClient.getInstance().textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
        MinecraftClient.getInstance().textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).pushFilter(false, false)
        GlStateManager.pushMatrix()
        ModelTransformation.applyGl(bakedModel.transformation.getTransformation(ModelTransformation.Type.NONE), false)
        GlStateManager.translated(x,y, z)

        // Render Quads
        renderBakedModelColored(bakedModel, color)

        // Clean Up
        GlStateManager.cullFace(GlStateManager.FaceSides.BACK)
        GlStateManager.popMatrix()
    }

    fun renderQuad(bufferBuilder: BufferBuilder, bakedQuad: BakedQuad, color: Int) {
        bufferBuilder.putVertexData(bakedQuad.vertexData)
        bufferBuilder.setQuadColor(color)
        val vec = bakedQuad.face.vector
        bufferBuilder.postNormal(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
    }
}
