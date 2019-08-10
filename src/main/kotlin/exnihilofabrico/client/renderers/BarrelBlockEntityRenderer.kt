package exnihilofabrico.client.renderers

import exnihilofabrico.common.barrels.BarrelBlockEntity
import net.minecraft.client.render.block.entity.BlockEntityRenderer

class BarrelBlockEntityRenderer: BlockEntityRenderer<BarrelBlockEntity>() {
    private val xzScale = 0.875
    private val yMin = 0.1875
    private val yMax = 0.9375

/*    override fun render(barrel: BarrelBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val mode = barrel?.mode ?: return

        when(mode) {
            is FluidMode -> renderFluid(barrel, mode, x, y, z)
            is BlockMode -> renderBlock(mode, x, y, z)
        }
    }

    fun renderFluid(barrel: BarrelBlockEntity, mode: FluidMode, x: Double, y: Double, z: Double) {
        val level = mode.fluid.amount.toDouble() / DropletValues.BUCKET.toDouble()
        val yScale = yMax - (yMax-yMin)*level
        val stack = mode.fluid.fluid.defaultState.blockState.block.asStack()

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+0.625+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(stack, ModelTransformation.Type.NONE)
        GlStateManager.popMatrix()
    }

    fun renderBlock(mode: BlockMode, x: Double, y: Double, z: Double) {
        val yScale = yMax - (yMax-yMin)
        val block = mode.contents

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+0.625+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(block, ModelTransformation.Type.NONE)
        GlStateManager.popMatrix()
    }*/
}