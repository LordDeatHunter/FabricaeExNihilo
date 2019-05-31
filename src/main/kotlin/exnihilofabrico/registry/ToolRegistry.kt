package exnihilofabrico.registry

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.registry.IToolRegistry
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import java.util.*

data class ToolRegistry(val registry: MutableMap<Ingredient, MutableList<Lootable>> = HashMap()): IToolRegistry {
    override fun registerDrops(target: Ingredient, loot: Collection<Lootable>) {
        if(!registry.containsKey(target))
            registry[target] = mutableListOf()
        registry[target]?.addAll(loot)
    }

    override fun isRegistered(target: ItemConvertible): Boolean {
        return registry.any { it.key.test(ItemStack(target)) }
    }

    override fun getResult(target: ItemConvertible, rand: Random) =
            getAllResults(target).filter { loot -> loot.chance.any { rand.nextDouble() < it } }.map { it.stack }
    override fun getAllResults(target: ItemConvertible): List<Lootable> {
        return registry.filter { it.key.test(ItemStack(target)) }.values.flatten()
    }

}