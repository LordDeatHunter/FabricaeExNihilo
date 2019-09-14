package exnihilofabrico.common.ore

import exnihilofabrico.common.base.BaseItem
import exnihilofabrico.id
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class OreChunkItem(val properties: OreProperties, settings: Settings): BaseItem(settings) {
    init {
        this.addPropertyGetter(id("base_material")) { stack: ItemStack, _: World?, _: LivingEntity? ->
            ((stack.item as? OreChunkItem)?.properties?.matrix?.ordinal?.toFloat() ?: 0f) + 0.5f
        }
        this.addPropertyGetter(id("shape")) { stack: ItemStack, _: World?, _: LivingEntity? ->
            ((stack.item as? OreChunkItem)?.properties?.chunk?.ordinal?.toFloat() ?: 0f) + 0.5f
        }
    }
}