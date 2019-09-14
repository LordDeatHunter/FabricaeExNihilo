package exnihilofabrico.client

import exnihilofabrico.id
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier
import java.util.function.Function

object OrePieceUnbakedModel: UnbakedModel {
    override fun getModelDependencies(): MutableCollection<Identifier>
            = mutableListOf<Identifier>()
    override fun getTextureDependencies(function: Function<Identifier, UnbakedModel>?, set: MutableSet<String>?)
            = mutableListOf<Identifier>()
    override fun bake(loader: ModelLoader, function: Function<Identifier, Sprite>, settings: ModelBakeSettings): BakedModel? {
        return loader.bakedModelMap.get(ModelIdentifier(id("ore_piece"), "inventory"))
    }
}