package exnihilofabrico.util

import exnihilofabrico.id
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

fun ItemStack.ofSize(num: Int = 1):ItemStack {
    if(num <= 0) return ItemStack.EMPTY
    val stack = this.copy()
    stack.count = num
    return stack
}

fun ItemConvertible.asStack(num: Int = 1) = ItemStack(this.asItem(), num)

fun getItemStack(identifier: Identifier) = Registry.ITEM[identifier].asStack()
fun getExNihiloItemStack(str: String) = getItemStack(id(str))
fun getExNihiloBlock(str: String) = Registry.BLOCK[id(str)]
fun getExNihiloItem(str: String) = Registry.ITEM[id(str)]

