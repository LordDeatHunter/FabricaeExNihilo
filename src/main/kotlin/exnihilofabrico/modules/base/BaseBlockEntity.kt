package exnihilofabrico.modules.base

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.server.world.ServerWorld

abstract class BaseBlockEntity(type: BlockEntityType<*>): BlockEntity(type) {
    fun updateClient() {
        (this.world as? ServerWorld)?.method_14178()?.markForUpdate(pos)
    }

    fun markDirtyClient() {
        this.markDirty()
        this.updateClient()
    }
}