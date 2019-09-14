package exnihilofabrico.common.ore

import exnihilofabrico.common.base.BaseItem
import exnihilofabrico.id
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class OrePieceItem(val properties: OreProperties, settings: Settings): BaseItem(settings) {
    init {
        this.addPropertyGetter(id("base_material")) { stack: ItemStack, _: World?, _: LivingEntity? ->
            (stack.item as? OrePieceItem)?.properties?.matrix?.ordinal?.toFloat() ?: 0f
        }
        this.addPropertyGetter(id("shape")) { stack: ItemStack, _: World?, _: LivingEntity? ->
            (stack.item as? OrePieceItem)?.properties?.piece?.ordinal?.toFloat() ?: 0f
        }
    }
}