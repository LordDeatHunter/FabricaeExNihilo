package exnihilofabrico.util

import net.minecraft.item.ItemStack

fun ItemStack.ofSize(num: Int = 1):ItemStack {
    val stack = this.copy()
    stack.amount = num
    return stack
}