package exnihilofabrico.client.renderers

import exnihilofabrico.modules.sieves.SieveBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

class SieveBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<SieveBlockEntity>(dispatcher) {
    private val xzScale = 0.875f
    private val yMin = 0.0625f
    private val yMax = 0.3750f

    override fun render(sieve: SieveBlockEntity?, partialTicks: Float, matrixStack: MatrixStack, vertexConsumerProvider: VertexConsumerProvider?, light: Int, overlays: Int) {

        val mesh = sieve?.mesh ?: return
        val contents = sieve.contents
        val progress = sieve.progress

        val x= sieve.pos.x.toDouble()
        val y= sieve.pos.y.toDouble()
        val z= sieve.pos.z.toDouble()

        // Render the Mesh
        renderMesh(matrixStack, x, y, z, mesh, light, overlays, vertexConsumerProvider)
        renderContents(matrixStack, x, y, z, contents, progress.toFloat(), light, overlays, vertexConsumerProvider)
    }

    fun renderMesh(matrixStack: MatrixStack, x: Double, y: Double, z: Double, mesh: ItemStack, light: Int, overlays: Int, vertexConsumerProvider: VertexConsumerProvider?) {
        if(mesh.isEmpty)
            return
        matrixStack.push()
        matrixStack.translate(x + 0.5, y + 0.5, z + 0.5)
        MinecraftClient.getInstance().itemRenderer.renderItem(mesh, ModelTransformation.Mode.NONE, light, overlays, matrixStack, vertexConsumerProvider)
        matrixStack.pop()
    }

    fun renderContents(matrixStack: MatrixStack, x: Double, y: Double, z: Double, contents: ItemStack, progress: Float, light: Int, overlays: Int, vertexConsumerProvider: VertexConsumerProvider?) {
        if(contents.isEmpty)
            return
        val yScale = yMax - (yMax-yMin)*progress

        matrixStack.push()
        matrixStack.translate(x+0.5,y+0.625+yScale/2, z+0.5)
        matrixStack.scale(xzScale,yScale,xzScale)
        MinecraftClient.getInstance().itemRenderer.renderItem(contents, ModelTransformation.Mode.NONE, light, overlays, matrixStack, vertexConsumerProvider)
        matrixStack.pop()
    }
}