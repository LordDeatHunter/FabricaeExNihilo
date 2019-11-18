package exnihilofabrico.registry.barrel

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.recipes.barrel.AlchemyRecipe
import exnihilofabrico.api.registry.IAlchemyRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

data class AlchemyRegistry(val registry: MutableList<AlchemyRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<AlchemyRecipe>>(), IAlchemyRegistry {

    override fun clear() = registry.clear()
    override fun serializable() = registry

    override fun register(recipe: AlchemyRecipe): Boolean {
        val conflict = registry.any { it.reactant == recipe.reactant && it.catalyst == recipe.catalyst }

        if(conflict) {
            ExNihiloFabrico.LOGGER.warn("Conflicting Alchemy Recipe not registered: $recipe")
            return false
        }

        return registry.add(recipe)
    }

    override fun getRecipe(reactant: FluidVolume, catalyst: ItemStack) =
        registry.firstOrNull { it.test(reactant, catalyst) }

    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<AlchemyRecipe> = gson.fromJson(
                FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE: Type = object : TypeToken<MutableList<AlchemyRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { AlchemyRegistry() },
            MetaModule::registerAlchemy
        )
    }
}