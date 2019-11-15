package exnihilofabrico.client.renderers

import com.mojang.blaze3d.platform.GlStateManager
import exnihilofabrico.modules.sieves.SieveBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.item.ItemStack

class SieveBlockEntityRenderer : BlockEntityRenderer<SieveBlockEntity>() {
    private val xzScale = 0.875
    private val yMin = 0.0625
    private val yMax = 0.3750

    override fun render(sieveBlockEntity: SieveBlockEntity?, x: Double, y: Double, z: Double, partialTicks: Float, breakingStage: Int) {
        val mesh = sieveBlockEntity?.mesh ?: return
        val contents = sieveBlockEntity.contents
        val progress = sieveBlockEntity.progress
        if(mesh.isEmpty)
            return
        renderMesh(mesh, x, y, z)
        if(!contents.isEmpty)
            renderContents(contents, progress, x, y, z)
    }

    fun renderMesh(mesh: ItemStack, x: Double, y: Double, z: Double) {
        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+0.5, z+0.5)
        MinecraftClient.getInstance().itemRenderer.renderItem(mesh, ModelTransformation.Type.NONE)
        GlStateManager.popMatrix()
    }

    fun renderContents(contents: ItemStack, progress: Double, x: Double, y: Double, z: Double) {
        val yScale = yMax - (yMax-yMin)*progress

        GlStateManager.pushMatrix()
        GlStateManager.translated(x+0.5,y+0.625+yScale/2, z+0.5)
        GlStateManager.scaled(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(contents, ModelTransformation.Type.NONE)
        GlStateManager.popMatrix()
    }
}