package exnihilofabrico.client

import exnihilofabrico.util.Color
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.util.math.Direction
import java.util.*
import kotlin.collections.ArrayList

class ColorTintedBakedModel(val baseModel: BakedModel, val color: Color): BakedModel {

    override fun getQuads(state: BlockState?, direction: Direction?, random: Random?): MutableList<BakedQuad> {
        var baseQuads = baseModel.getQuads(state, direction, random)
        var quads = ArrayList<BakedQuad>()

        for(bakedQuad in baseQuads) {
            if( bakedQuad.hasColor()) {
                var vertex = bakedQuad.vertexData


            }
            else {
                quads.add(bakedQuad)
            }
        }
        return quads
    }

    override fun getItemPropertyOverrides(): ModelItemPropertyOverrideList = baseModel.itemPropertyOverrides
    override fun getSprite(): Sprite = baseModel.sprite
    override fun useAmbientOcclusion(): Boolean = baseModel.useAmbientOcclusion()
    override fun hasDepthInGui(): Boolean = baseModel.hasDepthInGui()
    override fun getTransformation(): ModelTransformation = baseModel.transformation
    override fun isBuiltin(): Boolean = false

}