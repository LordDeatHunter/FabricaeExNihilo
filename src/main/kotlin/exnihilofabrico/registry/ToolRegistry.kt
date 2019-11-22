package exnihilofabrico.registry

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.ToolRecipe
import exnihilofabrico.api.registry.IToolRegistry
import exnihilofabrico.util.ofSize
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*

data class ToolRegistry(val registry: MutableList<ToolRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<ToolRecipe>>(), IToolRegistry {

    override fun register(target: ItemIngredient, loot: Collection<Lootable>) {
        val match = registry.firstOrNull { target == it.ingredient }
        if(match == null)
            registry.add(ToolRecipe(target, loot.toMutableList()))
        else
            match.lootables.addAll(loot)
    }

    override fun isRegistered(target: ItemConvertible) = registry.any { it.ingredient.test(target.asItem()) }

    override fun getResult(target: ItemConvertible, rand: Random): MutableList<ItemStack> {
        return getAllResults(target)
            .map { loot ->
                val amount = loot.chance.count { chance -> chance > rand.nextDouble() }
                if(amount > 0) loot.stack.ofSize(amount) else ItemStack.EMPTY
            }
            .filter { !it.isEmpty }
            .toMutableList()
    }
    override fun getAllResults(target: ItemConvertible) =
        registry.filter { it.ingredient.test(target.asItem()) }.map { it.lootables }.flatten()

    override fun registerJson(file: File) {
        val json: MutableList<ToolRecipe> = gson.fromJson(FileReader(file), SERIALIZATION_TYPE)
        json.forEach { register(it.ingredient, it.lootables) }
    }
    override fun serializable() = registry

    companion object {
        val SERIALIZATION_TYPE: Type = object: TypeToken<MutableList<ToolRecipe>>(){}.type
        fun fromJson(file: File, defaults: (IToolRegistry) -> Unit) = fromJson(file, {ToolRegistry()}, defaults)
    }

}