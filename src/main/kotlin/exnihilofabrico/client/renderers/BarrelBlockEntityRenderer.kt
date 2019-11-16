package exnihilofabrico.client.renderers

import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.modules.barrels.BarrelBlockEntity
import exnihilofabrico.modules.barrels.modes.AlchemyMode
import exnihilofabrico.modules.barrels.modes.CompostMode
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11
import kotlin.math.pow

class BarrelBlockEntityRenderer: BlockEntityRenderer<BarrelBlockEntity>() {
    private val xzScale = 12.0 / 16.0
    private val yMin = 0.1875
    private val yMax = 0.9375

    override fun render(barrel: BarrelBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val mode = barrel?.mode ?: return

        when(mode) {
            is FluidMode -> renderFluidMode(mode, barrel.pos)
            is ItemMode -> renderItem(mode.stack, 1.0, x, y, z)
            is AlchemyMode -> renderAlchemyMode(mode, barrel.pos)
            is CompostMode -> renderCompostMode(mode, x, y, z)
        }
    }

    private fun renderAlchemyMode(mode: AlchemyMode, pos: BlockPos) {

    }

    private fun renderCompostMode(mode: CompostMode, x: Double, y: Double, z: Double) {
        val yScale = (yMax-yMin) * minOf(mode.amount, 1.0)

        val color = Color.average(Color.WHITE, mode.color, mode.progress.pow(2)).toInt()

        GlStateManager.pushMatrix()
        GlStateManager.translated(x + 0.125,y + yMin, z + 0.125)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        RenderHelper.renderBakedModelColored(MinecraftClient.getInstance().itemRenderer.getModel(mode.result), color)
        GlStateManager.popMatrix()
    }

    private fun renderFluidMode(mode: FluidMode, pos: BlockPos) {
        renderFluid(mode.fluid.getFluid(), mode.fluid.amount.toDouble() / FluidStack.BUCKET_AMOUNT, pos)
    }

    private fun renderFluid(fluid: Fluid, level: Double, pos: BlockPos) {
        val x = pos.x.toDouble()
        val y = pos.y.toDouble()
        val z = pos.z.toDouble()

        val spriteColor = RenderHelper.getFluidSpriteAndColor(world, pos, fluid)
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
        bufferBuilder.setQuadColor(color)
        bufferBuilder.vertex(2.0/16.0, yRender, 2.0/16.0).texture(spriteU0, spriteV0).next()
        bufferBuilder.vertex(2.0/16.0, yRender, 14.0/16.0).texture(spriteU0, spriteV1).next()
        bufferBuilder.vertex(14.0/16.0, yRender, 14.0/16.0).texture(spriteU1, spriteV1).next()
        bufferBuilder.vertex(14.0/16.0, yRender, 2.0/16.0).texture(spriteU1, spriteV0).next()

        tessellator.draw()
        GlStateManager.enableLighting()
        GlStateManager.popMatrix()
    }

    private fun renderItem(stack: ItemStack, level: Double, x: Double, y: Double, z: Double) {
        val yScale = (yMax-yMin)*level

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+yMin+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(stack, ModelTransformation.Type.NONE)
        GlStateManager.popMatrix()
    }
}