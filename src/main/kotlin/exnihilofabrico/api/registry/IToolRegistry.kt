package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.ItemTags
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream

interface IToolRegistry {
    fun registerDrops(target: Ingredient, loot: Collection<Lootable>)
    fun registerDrops(target: Identifier, loot: Collection<Lootable>) = registerDrops(Registry.BLOCK[target], loot)
    fun registerDrops(target: Identifier, vararg loot: Lootable) = registerDrops(Registry.BLOCK[target], loot.toList())
    fun registerDrops(target: Ingredient, vararg loot: Lootable) = registerDrops(target, loot.toList())
    fun registerDrops(target: ItemConvertible, loot: Collection<Lootable>) = registerDrops(Ingredient.ofItems(target), loot)
    fun registerDrops(target: ItemConvertible, vararg loot: Lootable) = registerDrops(Ingredient.ofItems(target), loot.toList())

    fun registerTag(target: Tag<Item>, loot: Collection<Lootable>) = registerDrops(Ingredient.fromTag(target), loot)
    fun registerTag(target: Tag<Item>, vararg loot: Lootable) = registerDrops(Ingredient.fromTag(target), loot.toList())

    fun isRegistered(target: ItemConvertible): Boolean

    fun getResult(target: ItemConvertible, rand: Random): MutableList<ItemStack>
    fun getAllResults(target: ItemConvertible): List<Lootable>
}