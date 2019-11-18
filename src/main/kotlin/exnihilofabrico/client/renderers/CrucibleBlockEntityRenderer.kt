package exnihilofabrico.client.renderers

import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.modules.crucibles.CrucibleBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GuiLighting
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class CrucibleBlockEntityRenderer: BlockEntityRenderer<CrucibleBlockEntity>() {
    private val xzScale = 12.0/16.0
    private val xMin = 2.0 / 16.0
    private val xMax = 14.0 / 16.0
    private val zMin = 2.0 / 16.0
    private val zMax = 14.0 / 16.0
    private val yMin = 5.0/16.0
    private val yMax = 15.0/16.0

    override fun render(crucible: CrucibleBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val contents = crucible?.contents ?: return
        val queued = crucible.queued

        if(!contents.isEmpty()) {
            renderFluidVolume(crucible, contents, contents.amount.toDouble() / crucible.getMaxCapacity(), x, y, z)
        }
        val render = crucible.render
        if(!queued.isEmpty() && !render.isEmpty) {
            renderQueued(render, queued.amount.toDouble() / crucible.getMaxCapacity(), x, y, z)
        }
    }

    fun renderFluidVolume(blockentity: BlockEntity, volume: FluidVolume, level: Double, x: Double, y: Double, z: Double) {
        val yRender = (yMax-yMin)*level + yMin

        GuiLighting.disable()
        volume.render(listOf(FluidRenderFace.createFlatFace(xMin, yMin, zMin, xMax, yRender, zMax, 16.0, Direction.UP)), x, y, z)
        GuiLighting.enable()
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