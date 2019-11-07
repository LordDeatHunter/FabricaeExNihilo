package exnihilofabrico.registry

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.registry.IMeshRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.modules.sieves.MeshProperties
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import java.io.File
import java.io.FileReader

data class MeshRegistry(val registry: MutableList<MeshProperties> = mutableListOf()): AbstractRegistry<MutableList<MeshProperties>>(), IMeshRegistry {
    override fun clear() = registry.clear()
    override fun getAll() = registry
    override fun register(properties: MeshProperties) = registry.add(properties)
    override fun registerItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.identifier, it.item) }

    override fun serializable() = registry
    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<MeshProperties> = gson.fromJson(FileReader(file), SERIALIZATION_TYPE)
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE = object : TypeToken<MutableList<MeshProperties>>() {}.type
        fun fromJson(file: File) = fromJson(file, {MeshRegistry()}, MetaModule::registerMesh)
    }

}