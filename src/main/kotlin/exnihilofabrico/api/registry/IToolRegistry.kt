package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*

interface IToolRegistry {
    fun registerDrops(target: TagIngredient<Item>, loot: Collection<Lootable>)
    fun registerDrops(target: Identifier, loot: Collection<Lootable>) = registerDrops(Registry.BLOCK[target], loot)
    fun registerDrops(target: Identifier, vararg loot: Lootable) = registerDrops(Registry.BLOCK[target], loot.toList())
    fun registerDrops(target: TagIngredient<Item>, vararg loot: Lootable) = registerDrops(target, loot.toList())
    fun registerDrops(target: ItemConvertible, loot: Collection<Lootable>) = registerDrops(TagIngredient.fromMatches(target.asItem()), loot)
    fun registerDrops(target: ItemConvertible, vararg loot: Lootable) = registerDrops(TagIngredient.fromMatches(target.asItem()), loot.toList())

    fun registerDrops(target: Tag<Item>, loot: Collection<Lootable>) = registerDrops(TagIngredient.fromTags(target), loot)
    fun registerDrops(target: Tag<Item>, vararg loot: Lootable) = registerDrops(TagIngredient.fromTags(target), loot.toList())

    fun isRegistered(target: ItemConvertible): Boolean

    fun getResult(target: ItemConvertible, rand: Random): MutableList<ItemStack>
    fun getAllResults(target: ItemConvertible): List<Lootable>
}