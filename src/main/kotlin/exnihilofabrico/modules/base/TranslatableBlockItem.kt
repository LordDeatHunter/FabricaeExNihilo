package exnihilofabrico.modules.base

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class TranslatableBlockItem(block: Block, val translatableText: TranslatableText, settings: Settings): BlockItem(block, settings) {
    @Environment(EnvType.CLIENT)
    override fun getName() = translatableText
    override fun getName(stack: ItemStack): Text = this.name
}