package exnihilofabrico.client.renderers

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.texture.MissingSprite
import net.minecraft.client.texture.Sprite
import net.minecraft.fluid.Fluid
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.ExtendedBlockView
import java.util.*

object RenderHelper {
    fun renderBakedModelColored(bakedModel: BakedModel, color: Int) {

        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.bufferBuilder
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL)

        // Render Quads
        val random = Random()
        Direction.values().forEach {
            random.setSeed(42L)
            bakedModel.getQuads(null, it, random).forEach { quad -> renderQuadColored(bufferBuilder, quad, color) }
        }
        random.setSeed(42L)
        bakedModel.getQuads(null, null, random).forEach { renderQuadColored(bufferBuilder, it, color) }

        tessellator.draw()

    }

    fun renderBakedModel(bakedModel: BakedModel) {

        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.bufferBuilder
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL)

        // Render Quads
        val random = Random()
        Direction.values().forEach {
            random.setSeed(42L)
            bakedModel.getQuads(null, it, random).forEach { quad -> renderQuad(bufferBuilder, quad) }
        }
        random.setSeed(42L)
        bakedModel.getQuads(null, null, random).forEach { renderQuad(bufferBuilder, it) }

        tessellator.draw()

    }

    fun renderQuadColored(bufferBuilder: BufferBuilder, bakedQuad: BakedQuad, color: Int) {
        bufferBuilder.putVertexData(bakedQuad.vertexData)
        bufferBuilder.setQuadColor(color)
        val vec = bakedQuad.face.vector
        bufferBuilder.postNormal(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
    }

    fun renderQuad(bufferBuilder: BufferBuilder, bakedQuad: BakedQuad) {
        bufferBuilder.putVertexData(bakedQuad.vertexData)
        val vec = bakedQuad.face.vector
        bufferBuilder.postNormal(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
    }

    fun getFluidSpriteAndColor(view: ExtendedBlockView, pos: BlockPos, fluid: Fluid): Pair<Sprite, Int> {

        val fluidState = fluid.defaultState
        val fluidRenderHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid)
        val sprite = fluidRenderHandler.getFluidSprites(view, pos, fluidState).firstOrNull() ?: MissingSprite.getMissingSprite()
        val color = fluidRenderHandler.getFluidColor(view, pos, fluidState)

        return Pair(sprite, color)
    }
}