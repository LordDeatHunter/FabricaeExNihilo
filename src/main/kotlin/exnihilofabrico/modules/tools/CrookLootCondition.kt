package exnihilofabrico.modules.tools

import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters

object CrookLootCondition: LootCondition {
    override fun test(context: LootContext?): Boolean {
        if(context==null)
            return false

        val tool = context.get(LootContextParameters.TOOL) ?: return false

        return CrookTool.isCrook(tool)
    }

}