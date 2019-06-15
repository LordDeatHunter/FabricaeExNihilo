package exnihilofabrico.client.renderers

import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.common.crucibles.CrucibleBlockEntity
import io.github.prospector.silk.fluid.FluidInstance
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.item.ItemStack

class CrucibleBlockEntityRenderer: BlockEntityRenderer<CrucibleBlockEntity>() {
    private val xzScale = 0.875
    private val yMin = 0.1875
    private val yMax = 0.9375

    override fun render(crucible: CrucibleBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val render = crucible?.render ?: return
        val contents = crucible.contents
        val queued = crucible.queued

        if(!contents.isEmpty) {
            val level = contents.amount.toDouble() / crucible.maxCapacity
            renderContents(contents, level, x, y, z)

        }
        if(!queued.isEmpty && !render.isEmpty) {
            val level = queued.amount.toDouble() / crucible.maxCapacity
            renderQueued(render, level, x, y, z)
        }
    }

    fun renderContents(contents: FluidInstance, level: Double, x: Double, y: Double, z: Double) {
        val yScale = yMax - (yMax-yMin)*level

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+0.625+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        // TODO Render Fluid
        GlStateManager.popMatrix()
    }

    fun renderQueued(renderStack: ItemStack, level: Double, x: Double, y: Double, z: Double) {
        val yScale = yMax - (yMax-yMin)*level

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+0.625+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(renderStack, ModelTransformation.Type.NONE)
        GlStateManager.popMatrix()
    }
}