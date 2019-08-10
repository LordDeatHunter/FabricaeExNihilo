package exnihilofabrico.common.barrels

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.id
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag

class BarrelBlockEntity: BlockEntity(TYPE), BlockEntityClientSerializable {
    /**
     * NBT Serialization section
     */
    override fun toTag(tag: CompoundTag) = toTagWithoutWorldInfo(super.toTag(tag))

    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        if(tag==null){
            ExNihiloFabrico.LOGGER.warn("A barrel at $pos is missing data.")
            return
        }
        fromTagWithoutWorldInfo(tag)
    }

    override fun toClientTag(tag: CompoundTag?) = toTag(tag ?: CompoundTag())
    override fun fromClientTag(tag: CompoundTag?) = fromTag(tag ?: CompoundTag())

    private fun toTagWithoutWorldInfo(tag: CompoundTag): CompoundTag {
        return tag
    }

    private fun fromTagWithoutWorldInfo(tag: CompoundTag) {

    }

    companion object {
        val TYPE: BlockEntityType<BarrelBlockEntity> =
            BlockEntityType.Builder.create({BarrelBlockEntity()},
                ModBlocks.BARRELS.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("barrel")
    }
}