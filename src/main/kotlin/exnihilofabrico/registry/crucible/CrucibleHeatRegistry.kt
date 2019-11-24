package exnihilofabrico.registry.crucible

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.recipes.crucible.CrucibleHeatRecipe
import exnihilofabrico.api.registry.ICrucibleHeatRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

data class CrucibleHeatRegistry(val registry: MutableList<CrucibleHeatRecipe> = ArrayList()): AbstractRegistry<MutableList<CrucibleHeatRecipe>>(), ICrucibleHeatRegistry {

    override fun clear() = registry.clear()

    override fun register(recipe: CrucibleHeatRecipe)  = registry.add(recipe)

    override fun getHeat(block: Block) = registry.firstOrNull { it.test(block) }?.value ?: 0
    override fun getHeat(fluid: Fluid) = registry.firstOrNull { it.test(fluid) }?.value ?: 0
    override fun getHeat(item: Item) = registry.firstOrNull { it.test(item) }?.value ?: 0
    override fun getHeat(stack: ItemStack) = registry.firstOrNull { it.test(stack) }?.value ?: 0

    override fun serializable() = registry
    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<CrucibleHeatRecipe> = gson.fromJson(FileReader(file),
                SERIALIZATION_TYPE
            )
            json.forEach { register(it) }
        }
    }
    override fun getREIRecipes() = registry

    companion object {
        val SERIALIZATION_TYPE: Type = object: TypeToken<MutableList<CrucibleHeatRecipe>>(){}.type
        fun fromJson(file: File) = fromJson(
            file,
            { CrucibleHeatRegistry() },
            MetaModule::registerCrucibleHeat
        )
    }
}