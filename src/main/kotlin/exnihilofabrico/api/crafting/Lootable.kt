package exnihilofabrico.api.crafting

import exnihilofabrico.util.asStack
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

data class Lootable(val stack: ItemStack, val chance: List<Double>) {
        constructor(stack: ItemStack, vararg chances: Double): this(stack, chances.toList())
        constructor(result: ItemConvertible, vararg chances: Double): this(result.asStack(), chances.toList())
        constructor(result: ItemConvertible, chances: List<Double>): this(result.asStack(), chances)
        constructor(result: Identifier, chances: List<Double>): this(Registry.ITEM[result].asStack(), chances)
        constructor(result: Identifier, vararg chances: Double): this(result, chances.toList())
}