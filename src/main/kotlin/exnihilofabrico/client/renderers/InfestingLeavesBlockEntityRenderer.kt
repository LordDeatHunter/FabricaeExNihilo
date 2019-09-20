package exnihilofabrico.client.renderers

import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.common.farming.InfestingLeavesBlockEntity
import exnihilofabrico.util.Color
import exnihilofabrico.util.asStack
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.util.math.Direction
import java.util.*


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

        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.bufferBuilder
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL)

        // Render Quads
        val random = Random()
        Direction.values().forEach {
            random.setSeed(42L)
            bakedModel.getQuads(null, it, random).forEach { this.renderQuad(bufferBuilder, it, color) }
        }
        random.setSeed(42L)
        bakedModel.getQuads(null, null, random).forEach { this.renderQuad(bufferBuilder, it, color) }

        tessellator.draw()

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
