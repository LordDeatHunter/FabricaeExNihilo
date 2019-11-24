package exnihilofabrico.registry.barrel

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.recipes.barrel.FluidOnTopRecipe
import exnihilofabrico.api.registry.IFluidOnTopRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.fluid.Fluid
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

class FluidOnTopRegistry(val registry: MutableList<FluidOnTopRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<FluidOnTopRecipe>>(), IFluidOnTopRegistry {

    override fun clear() = registry.clear()
    override fun register(recipe: FluidOnTopRecipe) = registry.add(recipe)

    override fun getResult(contents: Fluid, onTop: Fluid) =
        registry.firstOrNull { it.inBarrel.test(contents) && it.onTop.test(onTop) }?.result

    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<FluidOnTopRecipe> = gson.fromJson(
                FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }

    override fun serializable() = registry

    companion object {
        val SERIALIZATION_TYPE: Type = object : TypeToken<MutableList<FluidOnTopRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { FluidOnTopRegistry() },
            MetaModule::registerFluidOnTop
        )
    }
}