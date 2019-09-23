package exnihilofabrico.modules.farming

import exnihilofabrico.modules.base.BaseItem
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class TransformingItem(val fromBlock: Block, val toBlockState: BlockState, settings: Settings): BaseItem(settings) {

    constructor(fromBlock: Block, toBlock: Block, settings: Settings): this(fromBlock, toBlock.defaultState, settings)

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if(context.world.isClient)
            return ActionResult.SUCCESS
        val target = context.world.getBlockState(context.blockPos).block
        // TODO make this work if someone overrides a block
        if(target == fromBlock) {
            context.world.setBlockState(context.blockPos, toBlockState)
            return ActionResult.SUCCESS
        }
        return super.useOnBlock(context)
    }
}