package exnihilofabrico.content.crucibles

import exnihilofabrico.common.ModBlocks
import exnihilofabrico.id
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

class CrucibleBlockEntity: BlockEntity(TYPE) {

    companion object {
        val TYPE: BlockEntityType<CrucibleBlockEntity> = BlockEntityType.Builder.create({CrucibleBlockEntity()}, ModBlocks.CRUCIBLES_WOOD.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("crucible")
    }
}