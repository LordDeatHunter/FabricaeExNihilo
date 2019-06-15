package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*
import java.util.function.Predicate

interface IToolRegistry {
    fun registerDrops(target: Predicate<ItemStack>, loot: Collection<Lootable>)
    fun registerDrops(target: Identifier, loot: Collection<Lootable>) = registerDrops(Registry.BLOCK[target], loot)
    fun registerDrops(target: Identifier, vararg loot: Lootable) = registerDrops(Registry.BLOCK[target], loot.toList())
    fun registerDrops(target: Predicate<ItemStack>, vararg loot: Lootable) = registerDrops(target, loot.toList())
    fun registerDrops(target: ItemConvertible, loot: Collection<Lootable>) = registerDrops(Ingredient.ofItems(target), loot)
    fun registerDrops(target: ItemConvertible, vararg loot: Lootable) = registerDrops(Ingredient.ofItems(target), loot.toList())

    fun <T: ItemConvertible> registerTag(target: Tag<T>, loot: Collection<Lootable>) = registerDrops(TagIngredient(target), loot)
    fun <T: ItemConvertible> registerTag(target: Tag<T>, vararg loot: Lootable) = registerDrops(TagIngredient(target), loot.toList())

    fun isRegistered(target: ItemConvertible): Boolean

    fun getResult(target: ItemConvertible, rand: Random): MutableList<ItemStack>
    fun getAllResults(target: ItemConvertible): List<Lootable>
}