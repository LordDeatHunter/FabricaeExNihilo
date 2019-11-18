package exnihilofabrico.registry.witchwater

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.EntityTypeIngredient
import exnihilofabrico.api.recipes.witchwater.WitchWaterEntityRecipe
import exnihilofabrico.api.registry.IWitchWaterEntityRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.village.VillagerProfession
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

data class WitchWaterEntityRegistry(val registry: MutableList<WitchWaterEntityRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<WitchWaterEntityRecipe>>(), IWitchWaterEntityRegistry {

    override fun register(target: EntityTypeIngredient, profession: VillagerProfession?, spawn: EntityType<*>) =
        registry.add(WitchWaterEntityRecipe(target, profession, spawn))
    override fun clear() = registry.clear()
    override fun getAll() = registry

    override fun getSpawn(entity: Entity) = registry.firstOrNull { it.test(entity) }?.tospawn

    override fun serializable() = registry
    override fun registerJson(file: File) {
        val json: MutableList<WitchWaterEntityRecipe> = gson.fromJson(FileReader(file),
            SERIALIZATION_TYPE
        )
        json.forEach {
            register(it.target, it.profession, it.tospawn)
        }
    }

    companion object {
        val SERIALIZATION_TYPE: Type = object: TypeToken<MutableList<WitchWaterEntityRecipe>>(){}.type
        fun fromJson(file: File) = fromJson(
            file,
            { WitchWaterEntityRegistry() },
            MetaModule::registerWitchWaterEntity
        )

    }
}