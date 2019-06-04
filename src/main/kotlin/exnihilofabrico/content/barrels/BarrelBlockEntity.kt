package exnihilofabrico.content.barrels

import exnihilofabrico.common.ModBlocks
import exnihilofabrico.id
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

class BarrelBlockEntity: BlockEntity(TYPE) {

    companion object {
        val TYPE: BlockEntityType<BarrelBlockEntity> =
            BlockEntityType.Builder.create({BarrelBlockEntity()},
                ModBlocks.BARRELS.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("barrel")
    }
}