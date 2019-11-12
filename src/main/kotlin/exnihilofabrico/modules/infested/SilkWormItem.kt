package exnihilofabrico.modules.infested

import exnihilofabrico.modules.base.BaseItem
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class SilkWormItem(settings: Settings): BaseItem(settings) {
    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        if(context==null || context.world.isClient)
            return super.useOnBlock(context)

        val result = InfestedHelper.tryToInfest(context.world, context.blockPos)

        if(result == ActionResult.SUCCESS && context.player?.isCreative != true)
            context.stack.decrement(1)

        return result
    }
}