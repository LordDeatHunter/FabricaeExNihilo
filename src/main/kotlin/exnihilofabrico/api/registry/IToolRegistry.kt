package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.Lootable
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import java.util.*

interface IToolRegistry {
    fun registerDrops(target: Ingredient, vararg loot: Lootable) = registerDrops(target, loot.toList())
    fun registerDrops(target: Ingredient, loot: Collection<Lootable>)

    fun isRegistered(target: ItemConvertible): Boolean

    fun getResult(target: ItemConvertible, rand: Random): List<ItemStack>
    fun getAllResults(target: ItemConvertible): List<Lootable>
}