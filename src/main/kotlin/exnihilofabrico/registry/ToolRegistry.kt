package exnihilofabrico.registry

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.registry.IToolRegistry
import exnihilofabrico.util.StackFromId
import exnihilofabrico.util.asStack
import exnihilofabrico.util.ofSize
import net.minecraft.block.Blocks
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import java.util.*
import java.util.function.Predicate

data class ToolRegistry(val registry: MutableMap<Predicate<ItemStack>, MutableList<Lootable>> = HashMap()): IToolRegistry {
    override fun registerDrops(target: Predicate<ItemStack>, loot: Collection<Lootable>) {
        if(!registry.containsKey(target))
            registry[target] = mutableListOf()
        registry[target]?.addAll(loot)
    }

    override fun isRegistered(target: ItemConvertible) = registry.keys.any { it.test(target.asStack()) }

    override fun getResult(target: ItemConvertible, rand: Random): MutableList<ItemStack> {
        return getAllResults(target)
            .filter { loot-> loot.chance.any {it > rand.nextDouble() }}
            .map(Lootable::stack).toMutableList()
    }
    override fun getAllResults(target: ItemConvertible) =
        registry.filter { it.key.test(target.asStack()) }.values.flatten()

}