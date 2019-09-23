package exnihilofabrico.modules.infested

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.base.BaseBlockEntity
import exnihilofabrico.modules.base.IHasColor
import exnihilofabrico.util.Color
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.LeavesBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.MinecraftClient
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.Tickable
import net.minecraft.util.registry.Registry

class InfestingLeavesBlockEntity: BaseBlockEntity(TYPE), BlockEntityClientSerializable, Tickable, IHasColor {

    var infestedBlock: InfestedLeavesBlock = ModBlocks.INFESTED_LEAVES.values.first()
    var progress = 0.0

    var tickCounter = world?.random?.nextInt(ExNihiloFabrico.config.modules.silkworms.updateFrequency) ?: 0

    override fun tick() {
        // Don't update every single tick
        tickCounter += 1
        if(tickCounter < ExNihiloFabrico.config.modules.silkworms.updateFrequency) return
        tickCounter = 0

        // Advance
        progress += ExNihiloFabrico.config.modules.silkworms.progressPerUpdate

        if(progress < 1f) {
            markDirty()
            if(progress > ExNihiloFabrico.config.modules.silkworms.minimumSpreadPercent)
                InfestedHelper.tryToSpreadFrom(
                    world ?: return,
                    pos,
                    ExNihiloFabrico.config.modules.silkworms.infestingSpreadAttempts
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
        tag.putDouble("progress", progress)
        return tag
    }

    fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        infestedBlock = Registry.BLOCK[Identifier(tag.getString("block"))] as? InfestedLeavesBlock
            ?: ModBlocks.INFESTED_LEAVES.values.first()
        progress = tag.getDouble("progress")
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