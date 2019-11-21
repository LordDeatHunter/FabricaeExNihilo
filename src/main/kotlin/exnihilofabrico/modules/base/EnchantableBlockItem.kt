package exnihilofabrico.modules.base

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack

class EnchantableBlockItem(block: Block, val _enchantability: Int, settings: Settings): BlockItem(block, settings) {

    override fun isEnchantable(stack: ItemStack?) = true
    override fun getEnchantability(): Int {
        return _enchantability
    }
}