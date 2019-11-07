package exnihilofabrico.registry

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.ToolRecipe
import exnihilofabrico.json.*
import exnihilofabrico.modules.sieves.MeshProperties
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.WeightedList
import java.io.File
import java.io.FileWriter

abstract class AbstractRegistry<T>(
    val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .setLenient()
        .registerTypeAdapter(Identifier::class.java, IdentifierJson)
        .registerTypeAdapter(FluidIngredient::class.java, FluidIngredientJson)
        .registerTypeAdapter(Ingredient::class.java, IngredientJson)
        .registerTypeAdapter(ItemStack::class.java, ItemStackJson)
        .registerTypeAdapter(Block::class.java, BlockJson)
        .registerTypeAdapter(MeshProperties::class.java, MeshPropertiesJson)
        .registerTypeAdapter(WeightedList::class.java, WeightedListJson())
        //.registerTypeAdapter(ToolRecipe::class.java, ToolRecipeJson)
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