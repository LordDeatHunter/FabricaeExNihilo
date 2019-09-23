package exnihilofabrico.client.renderers

import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.client.renderers.RenderHelper.getFluidSpriteAndColor
import exnihilofabrico.modules.crucibles.CrucibleBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import org.lwjgl.opengl.GL11

class CrucibleBlockEntityRenderer: BlockEntityRenderer<CrucibleBlockEntity>() {
    private val xzScale = 12.0/16.0
    private val yMin = 5.0/16.0
    private val yMax = 15.0/16.0

    override fun render(crucible: CrucibleBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val contents = crucible?.contents ?: return
        val queued = crucible.queued

        if(!contents.isEmpty()) {
            renderContents(contents.fluid, contents.amount.toDouble() / crucible.getMaxCapacity(), crucible.pos, x, y, z)
        }
        val render = crucible.render
        if(!queued.isEmpty() && !render.isEmpty) {
            val level = queued.amount.toDouble() / crucible.getMaxCapacity()
            renderQueued(render, level, x, y, z)
        }
    }

    fun renderContents(contents: Identifier, level: Double, pos: BlockPos, x: Double, y: Double, z: Double) {
        val spriteColor = getFluidSpriteAndColor(world, pos, Registry.FLUID[contents])
        val sprite = spriteColor.first
        val color = spriteColor.second
        val yRender = (yMax-yMin)*level + yMin

        GlStateManager.pushMatrix()
        GlStateManager.disableLighting()
        GlStateManager.translated(x,y,z)

        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.bufferBuilder

        val spriteV0 = sprite.getV(2.0).toDouble()
        val spriteV1 = sprite.getV(14.0).toDouble()
        val spriteU0 = sprite.getU(2.0).toDouble()
        val spriteU1 = sprite.getU(14.0).toDouble()
        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV)
        bufferBuilder.vertex(2.0/16.0, yRender, 2.0/16.0).texture(spriteU0, spriteV0).next()
        bufferBuilder.vertex(2.0/16.0, yRender, 14.0/16.0).texture(spriteU0, spriteV1).next()
        bufferBuilder.vertex(14.0/16.0, yRender, 14.0/16.0).texture(spriteU1, spriteV1).next()
        bufferBuilder.vertex(14.0/16.0, yRender, 2.0/16.0).texture(spriteU1, spriteV0).next()

        tessellator.draw()
        GlStateManager.enableLighting()
        GlStateManager.popMatrix()
    }

    fun renderQueued(renderStack: ItemStack, level: Double, x: Double, y: Double, z: Double) {
        val yScale = (yMax-yMin)*level

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+yMin+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(renderStack, ModelTransformation.Type.NONE)
        GlStateManager.popMatrix()
    }
}