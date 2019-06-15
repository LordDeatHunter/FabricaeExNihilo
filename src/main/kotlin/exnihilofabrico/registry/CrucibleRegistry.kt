package exnihilofabrico.registry

import exnihilofabrico.api.recipes.CrucibleRecipe
import exnihilofabrico.api.registry.ICrucibleRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.*

data class CrucibleRegistry(val registry: MutableList<CrucibleRecipe> = ArrayList()): ICrucibleRegistry {

    override fun clear() = registry.clear()

    override fun register(recipe: CrucibleRecipe) {
        registry.add(recipe)
    }

    override fun getResult(item: Item) = registry.firstOrNull { it.input.test(ItemStack(item)) }?.output
}