package exnihilofabrico.registry.barrel

import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.recipes.MilkingRecipe
import exnihilofabrico.api.registry.IMilkingRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.entity.Entity
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type


data class MilkingRegistry(val registry: MutableList<MilkingRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<MilkingRecipe>>(), IMilkingRegistry {

    override fun serializable() = registry
    override fun clear() = registry.clear()

    override fun register(recipe: MilkingRecipe): Boolean {
        if(registry.any { it.entity == recipe.entity }) {
            ExNihiloFabrico.LOGGER.warn("Conflicting Milking Recipe not registered: $recipe")
            return false
        }
        return registry.add(recipe)
    }

    override fun getResult(entity: Entity): Pair<FluidStack, Int>? {
        val match = registry.firstOrNull { it.entity.test(entity) } ?: return null
        return match.result to match.cooldown
    }

    override fun hasResult(entity: Entity) = registry.any { it.entity.test(entity) }

    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<MilkingRecipe> = gson.fromJson(
                FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE: Type? = object : TypeToken<MutableList<MilkingRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { MilkingRegistry() },
            MetaModule::registerMilking
        )
    }

}