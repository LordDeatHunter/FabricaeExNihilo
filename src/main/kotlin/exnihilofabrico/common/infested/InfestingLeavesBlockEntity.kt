package exnihilofabrico.common.infested

import exnihilofabrico.ExNihiloConfig
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.common.base.IHasColor
import exnihilofabrico.id
import exnihilofabrico.util.Color
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.LeavesBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.MinecraftClient
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.Tickable
import net.minecraft.util.registry.Registry

class InfestingLeavesBlockEntity: BlockEntity(TYPE), BlockEntityClientSerializable, Tickable, IHasColor {

    var infestedBlock: InfestedLeavesBlock = ModBlocks.INFESTED_LEAVES.values.first()
    var progress = 0.0f

    var tickCounter = world?.random?.nextInt(ExNihiloConfig.Modules.Farming.InfestedLeaves.updateFrequency) ?: 0

    override fun tick() {
        // Don't update every single tick
        tickCounter += 1
        if(tickCounter < ExNihiloConfig.Modules.Farming.InfestedLeaves.updateFrequency) return
        tickCounter = 0

        // Advance
        progress += ExNihiloConfig.Modules.Farming.InfestedLeaves.progressPerUpdate

        if(progress < 1f) {
            markDirty()
            //world?.apply { updateListeners(pos, getBlockState(pos), getBlockState(pos), 3) }
            if(progress > ExNihiloConfig.Modules.Farming.InfestedLeaves.minimumSpreadPercent)
                InfestedHelper.tryToSpreadFrom(
                    world ?: return,
                    pos,
                    ExNihiloConfig.Modules.Farming.InfestedLeaves.infestingSpreadAttempts
                )
            return
        }

        // Done Transforming
        val curState = world?.getBlockState(pos) ?: return
        val newState = infestedBlock.defaultState
            .with(LeavesBlock.DISTANCE, curState.get(LeavesBlock.DISTANCE))
            .with(LeavesBlock.PERSISTENT, curState.get(LeavesBlock.PERSISTENT))
        world!!.setBlockState(pos, newState)
    }

    /**
     * NBT Serialization section
     */
    override fun toTag(tag: CompoundTag) = toTagWithoutWorldInfo(super.toTag(tag))
    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        if(tag==null){
            ExNihiloFabrico.LOGGER.warn("A sieve at $pos is missing data.")
            return
        }
        fromTagWithoutWorldInfo(tag)
    }

    override fun toClientTag(tag: CompoundTag?) = toTag(tag ?: CompoundTag())
    override fun fromClientTag(tag: CompoundTag?) = fromTag(tag ?: CompoundTag())

    fun toTagWithoutWorldInfo(tag: CompoundTag): CompoundTag {
        tag.putString("block", Registry.BLOCK.getId(infestedBlock).toString())
        tag.putFloat("progress", progress)
        return tag
    }

    fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        infestedBlock = Registry.BLOCK[Identifier(tag.getString("block"))] as? InfestedLeavesBlock
            ?:
                ModBlocks.INFESTED_LEAVES.values.first()
        progress = tag.getFloat("progress")
    }

    override fun getColor(index: Int): Int {
        val originalColor = MinecraftClient.getInstance().blockColorMap.getColor(infestedBlock.leafBlock.defaultState, world, pos)
        return Color.average(Color.WHITE, Color(originalColor), progress).toInt()
    }

    companion object {
        val TYPE: BlockEntityType<InfestingLeavesBlockEntity> =
            BlockEntityType.Builder.create({ InfestingLeavesBlockEntity() }, arrayOf(ModBlocks.INFESTING_LEAVES)).build(null)
        val BLOCK_ENTITY_ID = id("infesting")
    }
}