package exnihilofabrico.modules.infested

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.modules.ModBlocks
import net.minecraft.block.Block
import net.minecraft.block.LeavesBlock
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

object InfestedHelper {
    fun tryToInfest(world: World, pos: BlockPos): ActionResult {
        if(world.isClient) return ActionResult.PASS
        val originalState = world.getBlockState(pos)
        if(!LEAF_TO_INFESTED.containsKey(originalState.block))
            return ActionResult.PASS
        val newState = ModBlocks.INFESTING_LEAVES.defaultState
            .with(LeavesBlock.DISTANCE, originalState.get(LeavesBlock.DISTANCE))
            .with(LeavesBlock.PERSISTENT, originalState.get(LeavesBlock.PERSISTENT))
        val infestedBlock = getInfestedLeavesBlock(originalState.block)

        world.setBlockState(pos, newState)

        val blockEntity = world.getBlockEntity(pos) as? InfestingLeavesBlockEntity ?: return ActionResult.PASS

        blockEntity.infestedBlock = infestedBlock
        blockEntity.markDirty()

        return ActionResult.SUCCESS
    }

    fun tryToSpreadFrom(world: World, pos: BlockPos, tries: Int = 1) {
        for(attempt in (0 until tries)) {
            if((world.random.nextFloat()) < ExNihiloFabrico.config.modules.silkworms.spreadChance) {
                val i = world.random.nextInt(3) - 1
                val j = world.random.nextInt(3) - 1
                val k = world.random.nextInt(3) - 1
                val newPos = pos.add(Vec3i(i,j,k))
                if(world.isHeightValidAndBlockLoaded(newPos))
                    tryToInfest(world, newPos)
            }
        }
    }

    private val LEAF_TO_INFESTED = ModBlocks.INFESTED_LEAVES.map { (_,v) -> v.leafBlock to v }.toMap()

    private fun getInfestedLeavesBlock(block: Block): InfestedLeavesBlock {
        if(LEAF_TO_INFESTED.isEmpty())
            ModBlocks.INFESTED_LEAVES.map { (_,v) -> v.leafBlock to v }.toMap()
        return LEAF_TO_INFESTED[block] ?: ModBlocks.INFESTED_LEAVES.values.first()
    }
}