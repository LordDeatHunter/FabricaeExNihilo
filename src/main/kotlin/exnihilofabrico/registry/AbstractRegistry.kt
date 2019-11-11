package exnihilofabrico.registry

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.recipes.*
import exnihilofabrico.json.*
import exnihilofabrico.modules.sieves.MeshProperties
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.WeightedList
import net.minecraft.village.VillagerProfession
import java.io.File
import java.io.FileWriter

abstract class AbstractRegistry<T>(
    val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .setLenient()
        .registerTypeAdapter(Item::class.java, ItemJson)
        .registerTypeAdapter(Fluid::class.java, FluidJson)
        .registerTypeAdapter(Block::class.java, BlockJson)
        .registerTypeAdapter(EntityType::class.java, EntityTypeJson)
        .registerTypeAdapter(Identifier::class.java, IdentifierJson)
        .registerTypeAdapter(Ingredient::class.java, IngredientJson)
        .registerTypeAdapter(ItemStack::class.java, ItemStackJson)
        .registerTypeAdapter(MeshProperties::class.java, MeshPropertiesJson)
        .registerTypeAdapter(WeightedList::class.java, WeightedListJson())
        .registerTypeAdapter(ToolRecipe::class.java, ToolRecipeJson)
        .registerTypeAdapter(CrucibleHeatRecipe::class.java, CrucibleHeatRecipeJson)
        .registerTypeAdapter(SieveRecipe::class.java, SieveRecipeJson)
        .registerTypeAdapter(WitchWaterWorldRecipe::class.java, WitchWaterWorldRecipeJson)
        .registerTypeAdapter(WitchWaterEntityRecipe::class.java, WitchWaterEntityRecipeJson)
        .registerTypeAdapter(VillagerProfession::class.java, VillagerProfessionJson)
        .enableComplexMapKeySerialization()
        .create()
) {
    abstract fun registerJson(file: File)
    abstract fun serializable(): T

    companion object {
        inline fun <reified T, U: AbstractRegistry<T>> fromJson(file: File, factory: () -> (U), defaults: (U) -> Unit): U {
            val registry = factory()
            if(file.exists() && ExNihiloFabrico.config.modules.general.useJsonRecipes) {
                registry.registerJson(file)
            }
            else {
                defaults(registry)
                if(ExNihiloFabrico.config.modules.general.useJsonRecipes) {
                    try {
                        val fw = FileWriter(file)
                        registry.gson.toJson(registry.serializable(), object: TypeToken<T>(){}.type, fw)
                        fw.close()
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return registry
        }
    }
}