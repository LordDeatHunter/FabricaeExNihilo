package exnihilofabrico.registry

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.WitchWaterWorldRecipe
import exnihilofabrico.api.registry.IWitchWaterFluidRegistry
import exnihilofabrico.api.registry.WeightedList
import exnihilofabrico.compatibility.modules.MetaModule
import net.minecraft.fluid.Fluid
import java.io.File
import java.io.FileReader
import java.util.*

data class WitchWaterFluidRegistry(val registry: MutableList<WitchWaterWorldRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<WitchWaterWorldRecipe>>(), IWitchWaterFluidRegistry {

    override fun clear() = registry.clear()

    override fun register(ingredient: TagIngredient<Fluid>, result: WeightedList) {
        val match = registry.firstOrNull { it.fluid == ingredient }
        if(match != null)
            match.results.amend(result)
        else {
            registry.add(WitchWaterWorldRecipe(ingredient, result))
        }
    }

    override fun isRegistered(fluid: Fluid) = registry.any { it.fluid.test(fluid) }

    override fun getResult(fluid: Fluid, rand: Random) = getAllResults(fluid)?.choose(rand)
    override fun getAllResults(fluid: Fluid): WeightedList? {
        registry.forEach {
            if(it.fluid.test(fluid))
                return it.results
        }
        return null
    }

    override fun registerJson(file: File) {
        val json: MutableList<WitchWaterWorldRecipe> = gson.fromJson(FileReader(file), SERIALIZATION_TYPE)
        json.forEach { register(it.fluid, it.results) }
    }

    override fun serializable() = registry

    companion object {
        val SERIALIZATION_TYPE = object: TypeToken<MutableList<WitchWaterWorldRecipe>>(){}.type
        // TODO fix serialization
        fun fromJson(file: File): WitchWaterFluidRegistry {
            val registry = WitchWaterFluidRegistry()
            MetaModule.registerWitchWaterFluid(registry)
            return registry
        } //fromJson(file, {WitchWaterFluidRegistry()}, MetaModule::registerWitchWaterFluid)
    }
}