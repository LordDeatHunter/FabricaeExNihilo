package exnihilofabrico.content.base

import net.minecraft.client.render.model.UnbakedModel

interface IHasModel {
    fun getModel(): UnbakedModel
}