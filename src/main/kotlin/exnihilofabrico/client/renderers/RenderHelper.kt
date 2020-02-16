package exnihilofabrico.client.renderers

object RenderHelper {
//    fun renderItemColored(stack: ItemStack, type: ModelTransformation.Mode, color: Color) {
//        if (!stack.isEmpty) {
//            this.renderItemColored(stack, type, color, false)
//        }
//    }
//    fun renderItemColored(stack: ItemStack, type: ModelTransformation.Mode, color: Color, flip: Boolean) {
//        if (!stack.isEmpty) {
//            val textureManager = MinecraftClient.getInstance().textureManager
//            val model = MinecraftClient.getInstance().itemRenderer.models.getModel(stack)
//            textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
//            textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX)?.setFilter(false, false)
//            GlStateManager.enableRescaleNormal()
//            GlStateManager.alphaFunc(516, 0.1f)
//            GlStateManager.enableBlend()
//            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
//            GlStateManager.pushMatrix()
//            val modelTransformation = model.transformation
//            ModelTransformation.applyGl(modelTransformation.getTransformation(type), flip)
//            if (areFacesFlippedBy(modelTransformation.getTransformation(type))) {
//                GlStateManager.cullFace(GlStateManager.FaceSides.FRONT)
//            }
//            this.renderItemAndGlowWithColor(stack, model, color)
//            GlStateManager.cullFace(GlStateManager.FaceSides.BACK)
//            GlStateManager.popMatrix()
//            GlStateManager.disableRescaleNormal()
//            GlStateManager.disableBlend()
//            textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
//            textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).popFilter()
//        }
//    }
//
//    private fun areFacesFlippedBy(transformation: Transformation): Boolean {
//        return (transformation.scale.x < 0.0f) xor (transformation.scale.y < 0.0f) xor (transformation.scale.z < 0.0f)
//    }
//
//    fun renderItemAndGlowWithColor(stack: ItemStack, model: BakedModel, color: Color) {
//        if (!stack.isEmpty) {
//            GlStateManager.pushMatrix()
//            GlStateManager.translatef(-0.5f, -0.5f, -0.5f)
//            if (model.isBuiltin) {
//                GlStateManager.color4f(color.r, color.b, color.g, color.a)
//                GlStateManager.enableRescaleNormal()
//                ItemDynamicRenderer.INSTANCE.render(stack)
//            }
//            else {
//                renderItemModel(model, color.toInt(), stack)
//                // TODO Glint
////                if (stack.hasEnchantmentGlint()) {
////                    renderGlint(MinecraftClient.getInstance().textureManager, { renderModelWithTint(model, -8372020) }, 8)
////                }
//            }
//            GlStateManager.popMatrix()
//        }
//    }
//
//    private fun renderItemModel(model: BakedModel, color: Int, stack: ItemStack) {
//        val tessellator = Tessellator.getInstance()
//        val builder = tessellator.buffer
//        builder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL)
////        builder.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL)
//        val random = Random()
//
//        Direction.values().forEach {
//            random.setSeed(42L)
//            this.renderQuads(builder, model.getQuads(null, it, random), color, stack)
//        }
//
//        random.setSeed(42L)
//        this.renderQuads(builder, model.getQuads(null, null, random), color, stack)
//        tessellator.draw()
//    }
//
//    private fun renderQuads(builder: BufferBuilder, quads: List<BakedQuad>, color: Int, stack: ItemStack) {
//        quads.forEach {
//            builder.renderQuadColored(it, color)
//        }
//    }
//
//    fun renderBakedModelColored(bakedModel: BakedModel, color: Int) {
//
//        val tessellator = Tessellator.getInstance()
//        val builder = tessellator.buffer
//        builder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL)
////        builder.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL)
//
//        // Render Quads
//        val random = Random()
//        Direction.values().forEach {
//            random.setSeed(42L)
//            bakedModel.getQuads(null, it, random).forEach { quad -> builder.renderQuadColored(quad, color) }
//        }
//        random.setSeed(42L)
//        bakedModel.getQuads(null, null, random).forEach { builder.renderQuadColored(it, color) }
//
//        tessellator.draw()
//
//    }
}

//fun BufferBuilder.renderQuadColored(quad: BakedQuad, color: Int) {
//    this.putVertexData(quad.vertexData)
//    this.setQuadColor(color)
//    val f = (color shr 16 and 255).toFloat() / 255.0f
//    val g = (color shr 8 and 255).toFloat() / 255.0f
//    val h = (color and 255).toFloat() / 255.0f
//    this.color(f, g, h, 1.0)
//    val vec = quad.face.vector
//    this.postNormal(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
//
//}