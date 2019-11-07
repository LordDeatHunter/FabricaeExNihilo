package exnihilofabrico.registry

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.recipes.CrucibleRecipe
import exnihilofabrico.api.registry.ICrucibleRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileReader
import java.util.*

data class CrucibleRegistry(val registry: MutableList<CrucibleRecipe> = ArrayList()): AbstractRegistry<MutableList<CrucibleRecipe>>(), ICrucibleRegistry {

    override fun clear() = registry.clear()
    override fun register(recipe: CrucibleRecipe) = registry.add(recipe)
    override fun getResult(item: Item) = registry.firstOrNull { it.input.test(ItemStack(item)) }?.output

    override fun serializable() = registry
    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<CrucibleRecipe> = gson.fromJson(FileReader(file), SERIALIZATION_TYPE)
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE = object : TypeToken<MutableList<CrucibleRecipe>>() {}.type
        fun fromJson(file: File, defaults: (CrucibleRegistry) -> Unit) = fromJson(file, {CrucibleRegistry()}, defaults)
    }
}