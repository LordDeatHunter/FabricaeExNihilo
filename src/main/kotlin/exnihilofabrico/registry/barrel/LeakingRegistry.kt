package exnihilofabrico.registry.barrel

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.recipes.barrel.LeakingRecipe
import exnihilofabrico.api.registry.ILeakingRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.block.Block
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

data class LeakingRegistry(val registry: MutableList<LeakingRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<LeakingRecipe>>(), ILeakingRegistry {

    override fun serializable() = registry
    override fun clear() = registry.clear()

    override fun register(recipe: LeakingRecipe): Boolean {
        if(registry.any { it.fluid == recipe.fluid && it.target == recipe.target}) {
            ExNihiloFabrico.LOGGER.warn("Conflicting Leaking Recipe not registered: $recipe")
            return false
        }
        return registry.add(recipe)
    }

    override fun getResult(block: Block, fluid: FluidVolume): Pair<Block, Int>? {
        val match =
            registry.firstOrNull { it.target.test(block) && it.fluid.test(fluid) && it.loss <= fluid.amount } ?: return null
        return match.result to match.loss
    }

    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<LeakingRecipe> = gson.fromJson(
                FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE: Type? = object : TypeToken<MutableList<LeakingRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { LeakingRegistry() },
            MetaModule::registerLeaking
        )
    }

}