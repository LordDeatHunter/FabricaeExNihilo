package exnihilofabrico.api.crafting

import exnihilofabrico.util.asStack
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

data class Lootable(
        val stack: ItemStack,
        val chance: List<Double>
) {
        constructor(stack: ItemStack, chance: Double): this(stack, listOf(chance))
        constructor(result: ItemConvertible, chance: Double): this(result.asStack(), listOf(chance))
        constructor(result: ItemConvertible, chances: List<Double>): this(result.asStack(), chances)
        constructor(result: Identifier, chances: List<Double>): this(net.minecraft.util.registry.Registry.ITEM[result].defaultStack, chances)
        constructor(result: Identifier, chance: Double): this(result, listOf(chance))
}