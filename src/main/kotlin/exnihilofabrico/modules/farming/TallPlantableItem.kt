package exnihilofabrico.modules.farming

import exnihilofabrico.modules.base.BaseItem
import net.minecraft.block.TallPlantBlock
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class TallPlantableItem(val plants: List<TallPlantBlock>, settings: Settings): BaseItem(settings) {

    constructor(plant: TallPlantBlock, settings: Settings): this(listOf(plant), settings)

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if(context.world.isClient)
            return ActionResult.SUCCESS
        val plantPos = context.blockPos.offset(context.side)
        for (plant in plants.shuffled()) {
            val lower = plant.defaultState.with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER)
            val upper = plant.defaultState.with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER)
            if(placementCheck(context) && lower.canPlaceAt(context.world, plantPos) && context.world.isAir(plantPos.up())){
                context.world.setBlockState(plantPos, lower)
                context.world.setBlockState(plantPos.up(), upper)
                if(context.player?.isCreative == false)
                    context.stack.decrement(1)
                return ActionResult.SUCCESS
            }
        }
        return super.useOnBlock(context)
    }

    fun placementCheck(context: ItemUsageContext): Boolean {
        return true
    }
}