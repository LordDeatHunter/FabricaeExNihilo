package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*

interface IToolRegistry {
    fun register(target: ItemIngredient, loot: Collection<Lootable>)
    fun register(target: Identifier, loot: Collection<Lootable>) = register(Registry.BLOCK[target], loot)
    fun register(target: Identifier, vararg loot: Lootable) = register(Registry.BLOCK[target], loot.toList())
    fun register(target: ItemIngredient, vararg loot: Lootable) = register(target, loot.toList())
    fun register(target: ItemConvertible, loot: Collection<Lootable>) = register(ItemIngredient(target.asItem()), loot)
    fun register(target: ItemConvertible, vararg loot: Lootable) = register(ItemIngredient(target.asItem()), loot.toList())
    fun register(target: ItemConvertible, result: ItemConvertible) = register(ItemIngredient(target.asItem()),Lootable(result, 1.0))
    fun register(target: ItemConvertible, result: ItemConvertible, vararg chances: Double) =
        register(ItemIngredient(target.asItem()), Lootable(result, *chances))

    fun register(target: Tag<Item>, loot: Collection<Lootable>) = register(ItemIngredient(target), loot)
    fun register(target: Tag<Item>, vararg loot: Lootable) = register(ItemIngredient(target), loot.toList())

    fun isRegistered(target: ItemConvertible): Boolean

    fun getResult(target: ItemConvertible, rand: Random): MutableList<ItemStack>
    fun getAllResults(target: ItemConvertible): List<Lootable>
}