package exnihilofabrico.impl.enchantments

import exnihilofabrico.modules.crucibles.CrucibleBlock
import exnihilofabrico.modules.sieves.MeshItem
import net.minecraft.block.BarrelBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack

object EnchantmentOverrides {
    fun isEfficiencyEnchantable(stack: ItemStack): Boolean {
        return (stack.item).let { item ->
            when (item) {
                is BlockItem -> when (item.block) {
                    is BarrelBlock, is CrucibleBlock -> true
                    else -> false
                }
                is MeshItem -> true
                else -> false
            }
        }
    }
    fun isFireAspectEnchantable(stack: ItemStack): Boolean {
        return (stack.item).let { item ->
            when (item) {
                is BlockItem -> when (item.block) {
                    is CrucibleBlock -> true
                    else -> false
                }
                is MeshItem -> true
                else -> false
            }
        }
    }
    fun isFortuneEnchantable(stack: ItemStack): Boolean {
        return (stack.item is MeshItem)
    }
}