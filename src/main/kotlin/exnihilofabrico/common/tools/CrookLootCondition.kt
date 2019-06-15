package exnihilofabrico.common.tools

import net.minecraft.world.loot.condition.LootCondition
import net.minecraft.world.loot.context.LootContext
import net.minecraft.world.loot.context.LootContextParameters

object CrookLootCondition: LootCondition {
    override fun test(context: LootContext?): Boolean {
        if(context==null)
            return false

        val tool = context.get(LootContextParameters.TOOL) ?: return false

        return CrookTool.isCrook(tool)
    }

}