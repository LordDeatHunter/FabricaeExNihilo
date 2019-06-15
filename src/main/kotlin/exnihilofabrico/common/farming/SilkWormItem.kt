package exnihilofabrico.common.farming

import exnihilofabrico.common.base.BaseItem
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class SilkWormItem(settings: Settings): BaseItem(settings) {
    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        if(context==null || context.world.isClient)
            return super.useOnBlock(context)



        return ActionResult.SUCCESS
    }
}