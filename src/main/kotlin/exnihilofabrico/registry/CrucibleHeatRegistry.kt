package exnihilofabrico.registry

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.CrucibleHeatRecipe
import exnihilofabrico.api.registry.ICrucibleHeatRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import java.io.File
import java.io.FileReader
import java.io.FileWriter

data class CrucibleHeatRegistry(val registry: MutableList<CrucibleHeatRecipe> = ArrayList()): AbstractRegistry<MutableList<CrucibleHeatRecipe>>(), ICrucibleHeatRegistry {

    override fun clear() = registry.clear()

    override fun register(recipe: CrucibleHeatRecipe)  = registry.add(recipe)
    override fun register(block: Ingredient, heat: Int) = registry.add(CrucibleHeatRecipe(block, null, heat))
    override fun register(fluid: FluidIngredient, heat: Int) = registry.add(CrucibleHeatRecipe(null, fluid, heat))

    override fun register(block: Block, heat: Int) = register(Ingredient.ofItems(block), heat)
    override fun register(fluid: Fluid, heat: Int) = register(FluidIngredient(fluid), heat)

    override fun getHeat(block: Block) = registry.firstOrNull { it.test(block) }?.value ?: 0
    override fun getHeat(fluid: Fluid) = registry.firstOrNull { it.test(fluid) }?.value ?: 0
    override fun getHeat(item: Item) = registry.firstOrNull { it.test(item) }?.value ?: 0
    override fun getHeat(stack: ItemStack) = registry.firstOrNull { it.test(stack) }?.value ?: 0

    override fun serializable() = registry
    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<CrucibleHeatRecipe> = gson.fromJson(FileReader(file), SERIALIZATION_TYPE)
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE = object: TypeToken<MutableList<CrucibleHeatRecipe>>(){}.type
        fun fromJson(file: File) = fromJson(file, {CrucibleHeatRegistry()}, MetaModule::registerCrucibleHeat)
    }
}