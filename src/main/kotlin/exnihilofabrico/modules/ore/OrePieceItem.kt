package exnihilofabrico.modules.ore

import exnihilofabrico.modules.base.BaseItem
import exnihilofabrico.modules.base.IHasColor
import exnihilofabrico.util.Color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class OrePieceItem(val properties: OreProperties, settings: Settings): BaseItem(settings), IHasColor {
    override fun getColor(index: Int) = if(index==1) properties.color.toInt() else Color.WHITE.toInt()

    @Environment(EnvType.CLIENT)
    override fun getName(): Text {
        return TranslatableText("item.exnihilofabrico.piece", TranslatableText(properties.displayName))
    }
    override fun getName(stack: ItemStack): Text {
        return TranslatableText("item.exnihilofabrico.piece", TranslatableText(properties.displayName))
    }
}