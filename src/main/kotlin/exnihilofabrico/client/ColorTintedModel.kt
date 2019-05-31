package exnihilofabrico.client

import exnihilofabrico.util.Color
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.texture.Sprite
import net.minecraft.util.Identifier
import java.util.function.Function

class ColorTintedModel(val baseModel: UnbakedModel, val color: Color): UnbakedModel {

    override fun getModelDependencies(): MutableCollection<Identifier> = baseModel.modelDependencies

    override fun bake(loader: ModelLoader?, textures: Function<Identifier, Sprite>?, rotations: ModelBakeSettings?): BakedModel? {
        val baked = baseModel.bake(loader, textures, rotations) ?: return null

        return ColorTintedBakedModel(baked, color)
    }

    override fun getTextureDependencies(p0: Function<Identifier, UnbakedModel>?, p1: MutableSet<String>?): MutableCollection<Identifier> =
            baseModel.getTextureDependencies(p0, p1)

}