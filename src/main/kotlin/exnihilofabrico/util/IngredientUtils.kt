package exnihilofabrico.util

import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import java.util.function.Predicate

fun Predicate<ItemStack>.test(item: ItemConvertible) = this.test(item.asStack())

