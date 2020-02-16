package exnihilofabrico.client.renderers

import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.mojang.blaze3d.systems.RenderSystem
import exnihilofabrico.modules.crucibles.CrucibleBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class CrucibleBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<CrucibleBlockEntity>(dispatcher) {
    private val xzScale = 12.0/16.0
    private val xMin = 2.0 / 16.0
    private val xMax = 14.0 / 16.0
    private val zMin = 2.0 / 16.0
    private val zMax = 14.0 / 16.0
    private val yMin = 5.0/16.0
    private val yMax = 15.0/16.0

    override fun render(crucible: CrucibleBlockEntity?, tickDelta: Float, matrices: MatrixStack?, vertexConsumer: VertexConsumerProvider?, light: Int, overlay: Int) {
        val contents = crucible?.contents ?: return
        val queued = crucible.queued
        if(!contents.isEmpty()) {
            renderFluidVolume(crucible, contents, contents.amount.toDouble() / crucible.getMaxCapacity(), matrices, vertexConsumer)
        }
        val render = crucible.render
        if(!queued.isEmpty() && !render.isEmpty) {
            renderQueued(render, queued.amount.toDouble() / crucible.getMaxCapacity(), matrices, vertexConsumer, light, overlay)
        }
    }

    fun renderFluidVolume(blockentity: BlockEntity, volume: FluidVolume, level: Double, matrices: MatrixStack?, vertexConsumer: VertexConsumerProvider?) {
        val yRender = (yMax-yMin)*level + yMin

        RenderSystem.disableLighting()
        volume.render(listOf(FluidRenderFace.createFlatFace(xMin, yMin, zMin, xMax, yRender, zMax, 16.0, Direction.UP)), vertexConsumer, matrices)
        RenderSystem.enableLighting()
    }

    fun renderQueued(renderStack: ItemStack, level: Double, matrices: MatrixStack?, vertexConsumer: VertexConsumerProvider?, light: Int, overlay: Int) {
        val yScale = (yMax-yMin)*level

//        GlStateManager.pushMatrix()
//        GlStateManager.translated(x+0.5,y+yMin+yScale/2, z+0.5)
//        GlStateManager.scaled(xzScale,yScale,xzScale)
//        MinecraftClient.getInstance().itemRenderer.renderItem(renderStack, ModelTransformation.Mode.NONE, light, overlay, matrices, vertexConsumer)
//        GlStateManager.popMatrix()
    }
}