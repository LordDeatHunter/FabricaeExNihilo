package exnihilofabrico.client.renderers

import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.modules.barrels.BarrelBlockEntity
import exnihilofabrico.modules.barrels.modes.AlchemyMode
import exnihilofabrico.modules.barrels.modes.CompostMode
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GuiLighting
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import kotlin.math.pow

class BarrelBlockEntityRenderer: BlockEntityRenderer<BarrelBlockEntity>() {
    private val xzScale = 12.0 / 16.0
    private val xMin = 2.0 / 16.0
    private val xMax = 14.0 / 16.0
    private val zMin = 2.0 / 16.0
    private val zMax = 14.0 / 16.0
    private val yMin = 0.1875
    private val yMax = 0.9375

    override fun render(barrel: BarrelBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val mode = barrel?.mode ?: return

        when(mode) {
            is FluidMode -> renderFluidMode(mode, x, y, z)
            is ItemMode -> renderItem(mode.stack, 1.0, x, y, z)
            is AlchemyMode -> renderAlchemyMode(mode, x, y, z)
            is CompostMode -> renderCompostMode(mode, x, y, z)
        }
    }

    private fun renderAlchemyMode(mode: AlchemyMode, x: Double, y: Double, z: Double) {
        when(mode.before) {
            is FluidMode -> renderFluidMode(mode.before, x, y, z)
            is ItemMode -> renderItem(mode.before.stack, 1.0, x, y, z)
            is AlchemyMode -> renderAlchemyMode(mode.before, x, y, z)
            is CompostMode -> renderCompostMode(mode.before, x, y, z)
        }
        when(mode.after) {
            is FluidMode -> renderFluidMode(mode.after, x, y, z)
            is ItemMode -> renderItem(mode.after.stack, 1.0, x, y, z)
            is AlchemyMode -> renderAlchemyMode(mode.after, x, y, z)
            is CompostMode -> renderCompostMode(mode.after, x, y, z)
        }
    }

    private fun renderCompostMode(mode: CompostMode, x: Double, y: Double, z: Double) {
        val yScale = (yMax-yMin) * minOf(mode.amount, 1.0)

        val color = Color.average(Color.WHITE, mode.color, mode.progress.pow(4))

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+yMin+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        GlStateManager.color4f(color.r, color.g, color.b, color.a)
        RenderHelper.renderItemColored(mode.result, ModelTransformation.Type.NONE, color)
        GlStateManager.popMatrix()
    }

    private fun renderFluidMode(mode: FluidMode, x: Double, y: Double, z: Double) {
        renderFluidVolume(mode.fluid, mode.fluid.amount.toDouble() / FluidVolume.BUCKET, x, y, z)
    }
    fun renderFluidVolume(volume: FluidVolume, level: Double, x: Double, y: Double, z: Double) {
        val yRender = (yMax-yMin)*level + yMin

        GuiLighting.disable()
        volume.render(listOf(FluidRenderFace.createFlatFace(xMin, yMin, zMin, xMax, yRender, zMax, 16.0, Direction.UP)), x, y, z)
        GuiLighting.enable()
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