package exnihilofabrico.registry.barrel

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.recipes.barrel.FluidTransformRecipe
import exnihilofabrico.api.registry.IFluidTransformRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

class FluidTransformRegistry(val registry: MutableList<FluidTransformRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<FluidTransformRecipe>>(), IFluidTransformRegistry {

    override fun clear() = registry.clear()
    override fun register(recipe: FluidTransformRecipe) = registry.add(recipe)

    override fun getResult(fluid: Fluid, block: Block) =
        registry.firstOrNull { it.inBarrel.test(fluid) && it.catalyst.test(block) }?.result

    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<FluidTransformRecipe> = gson.fromJson(
                FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }

    override fun serializable() = registry
    override fun getREIRecipes() = registry

    companion object {
        val SERIALIZATION_TYPE: Type = object : TypeToken<MutableList<FluidTransformRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { FluidTransformRegistry() },
            MetaModule::registerFluidTransform
        )
    }
}