package exnihilofabrico.registry.barrel

import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.recipes.barrel.CompostRecipe
import exnihilofabrico.api.registry.ICompostRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

data class CompostRegistry(val registry: MutableList<CompostRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<CompostRecipe>>(), ICompostRegistry {

    override fun serializable() = registry
    override fun clear() = registry.clear()

    override fun register(recipe: CompostRecipe): Boolean {
        val conflict = registry.any { it.ingredient == recipe.ingredient }

        if(conflict) {
            ExNihiloFabrico.LOGGER.warn("Conflicting Compost Recipe not registered: $recipe")
            return false
        }

        return registry.add(recipe)
    }

    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<CompostRecipe> = gson.fromJson(
                FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }
    override fun getRecipe(stack: ItemStack) = registry.firstOrNull { it.test(stack) }

    companion object {
        val SERIALIZATION_TYPE: Type = object : TypeToken<MutableList<CompostRecipe>>() {}.type
        fun fromJson(file: File) = fromJson(
            file,
            { CompostRegistry() },
            MetaModule::registerCompost
        )
    }

}