@file:JvmName("ItemUtil")
package exnihilocreatio.util

import exnihilocreatio.items.tools.ICrook
import exnihilocreatio.items.tools.IHammer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

fun isCrook(stack: ItemStack?): Boolean {
    if (stack == null)
        return false

    if (stack.item === Items.AIR)
        return false

    if (stack.item is ICrook)
        return (stack.item as ICrook).isCrook(stack)

    // Inspirations compatibility
    // Using ToolClass is the recommended method for compatible mods.
    return (stack.item.getToolClasses(stack).contains("crook"))
}

fun isHammer(stack: ItemStack?): Boolean {
    if (stack == null)
        return false

    if (stack.item === Items.AIR)
        return false

    if (stack.item is IHammer)
        return (stack.item as IHammer).isHammer(stack)

    return false

}