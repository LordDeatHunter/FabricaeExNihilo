package exnihilofabrico.common.infested

import exnihilofabrico.common.base.IHasColor
import exnihilofabrico.util.Color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class InfestedLeavesItem(block: InfestedLeavesBlock, settings: Settings): BlockItem(block, settings), IHasColor {
    override fun getColor(index: Int) = Color.WHITE.toInt()

    @Environment(EnvType.CLIENT)
    override fun getName() = TranslatableText("block.exnihilofabrico.infested", (this.block as InfestedLeavesBlock).leafBlock.name)
    override fun getName(stack: ItemStack): Text = this.name
}