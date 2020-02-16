package exnihilofabrico.client.renderers

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.modules.barrels.BarrelBlockEntity
import exnihilofabrico.modules.barrels.modes.AlchemyMode
import exnihilofabrico.modules.barrels.modes.CompostMode
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.util.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import kotlin.math.pow

class BarrelBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<BarrelBlockEntity>(dispatcher) {


    private val xzScale = 12.0f / 16.0f
    private val xMin = 2.0f / 16.0f
    private val xMax = 14.0f / 16.0f
    private val zMin = 2.0f / 16.0f
    private val zMax = 14.0f / 16.0f
    private val yMin = 0.1875f
    private val yMax = 0.9375f

    override fun render(barrel: BarrelBlockEntity?, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int, overlays: Int) {
        if(matrices == null || vertexConsumers == null)
            return
        val mode = barrel?.mode ?: return

        when(mode) {
            is FluidMode -> renderFluid(mode, barrel.pos, tickDelta, matrices, vertexConsumers, light, overlays)
            is ItemMode -> renderItemMode(mode, barrel.pos, tickDelta, matrices, vertexConsumers, light, overlays)
            is AlchemyMode -> renderAlchemy(mode, barrel.pos, tickDelta, matrices, vertexConsumers, light, overlays)
            is CompostMode -> renderCompost(mode, barrel.pos, tickDelta, matrices, vertexConsumers, light, overlays)
        }
    }
    fun renderFluid(mode: FluidMode, pos: BlockPos, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlays: Int) {

    }
    fun renderItemMode(mode: ItemMode, pos: BlockPos, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlays: Int) {
        renderItem(mode.stack, pos, tickDelta, matrices, vertexConsumers, light, overlays, 1.0f)
    }
    fun renderAlchemy(mode: AlchemyMode, pos: BlockPos, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlays: Int) {

    }
    fun renderCompost(mode: CompostMode, pos: BlockPos, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlays: Int) {

        val yScale = (yMax-yMin) * minOf(mode.amount.toFloat(), 1.0f)

        val color = Color.average(Color.WHITE, mode.color, mode.progress.pow(4))

//        matrices.push()
//        matrices.translate(pos.x+0.5,(pos.y+yMin+yScale/2).toDouble(), pos.z+0.5)
//        matrices.scale(xzScale,yScale,xzScale)
//        RenderHelper.renderItemColored(mode.result, ModelTransformation.NONE, color)
//        matrices.pop()
    }

    fun renderItem(stack: ItemStack, pos: BlockPos, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlays: Int, level: Float) {
        val yScale = (yMax-yMin)*level

        matrices.push()
        matrices.translate(pos.x+0.5,(pos.y+yMin+yScale/2).toDouble(), pos.z+0.5)
        matrices.scale(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(stack, ModelTransformation.Mode.NONE, light, overlays, matrices, vertexConsumers)
        matrices.pop()
    }

    @Deprecated("1.14 renderer")
    fun render(barrel: BarrelBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val mode = barrel?.mode ?: return

        when(mode) {
            is FluidMode -> renderFluidMode(mode, x, y, z)
//            is ItemMode -> renderItem(mode.stack, 1.0, x, y, z)
            is AlchemyMode -> renderAlchemyMode(mode, x, y, z)
            is CompostMode -> renderCompostMode(mode, x, y, z)
        }
    }

    private fun renderAlchemyMode(mode: AlchemyMode, x: Double, y: Double, z: Double) {
        when(mode.before) {
            is FluidMode -> renderFluidMode(mode.before, x, y, z)
//            is ItemMode -> renderItem(mode.before.stack, 1.0, x, y, z)
            is AlchemyMode -> renderAlchemyMode(mode.before, x, y, z)
            is CompostMode -> renderCompostMode(mode.before, x, y, z)
        }
        when(mode.after) {
            is FluidMode -> renderFluidMode(mode.after, x, y, z)
//            is ItemMode -> renderItem(mode.after.stack, 1.0, x, y, z)
            is AlchemyMode -> renderAlchemyMode(mode.after, x, y, z)
            is CompostMode -> renderCompostMode(mode.after, x, y, z)
        }
    }

    private fun renderCompostMode(mode: CompostMode, x: Double, y: Double, z: Double) {
        val yScale = (yMax-yMin) * minOf(mode.amount, 1.0)

        val color = Color.average(Color.WHITE, mode.color, mode.progress.pow(4))

//        GlStateManager.pushMatrix()
//        GlStateManager.translated(x+0.5,y+yMin+yScale/2, z+0.5)
//        GlStateManager.scaled(xzScale,yScale,xzScale)
//        GlStateManager.color4f(color.r, color.g, color.b, color.a)
//        RenderHelper.renderItemColored(mode.result, ModelTransformation.Type.NONE, color)
//        GlStateManager.popMatrix()
    }

    private fun renderFluidMode(mode: FluidMode, x: Double, y: Double, z: Double) {
        renderFluidVolume(mode.fluid, mode.fluid.amount.toDouble() / FluidVolume.BUCKET, x, y, z)
    }
    fun renderFluidVolume(volume: FluidVolume, level: Double, x: Double, y: Double, z: Double) {
        val yRender = (yMax-yMin)*level + yMin

//        GuiLighting.disable()
//        volume.render(listOf(FluidRenderFace.createFlatFace(xMin, yMin, zMin, xMax, yRender, zMax, 16.0, Direction.UP)), x, y, z)
//        GuiLighting.enable()
    }
}