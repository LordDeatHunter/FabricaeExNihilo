package exnihilofabrico.client

import exnihilofabrico.MODID
import exnihilofabrico.content.base.IHasModel
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


object Models {
    fun createVariantProvider(manager: ResourceManager): ModelVariantProvider {
        return ModelVariantProvider { resourceId, context -> getModel(manager, resourceId, context) }
    }
    fun getModel(manager: ResourceManager, resourceId: ModelIdentifier, context: ModelProviderContext): UnbakedModel? {
        if("inventory" == resourceId.variant || MODID != resourceId.namespace)
            return null
        val block = Registry.BLOCK.get(Identifier(MODID, resourceId.path))
        if(block is IHasModel)
            return block.getModel()
        return null
    }
}