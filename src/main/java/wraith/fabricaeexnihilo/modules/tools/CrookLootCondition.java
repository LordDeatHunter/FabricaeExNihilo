package wraith.fabricaeexnihilo.modules.tools;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

public class CrookLootCondition implements LootCondition {
    
    @Override
    public boolean test(LootContext lootContext) {
        if (lootContext == null) {
            return false;
        }
        var tool = lootContext.get(LootContextParameters.TOOL);
        
        return tool != null && CrookItem.isCrook(tool);
    }
    
    @Override
    public LootConditionType getType() {
        return null;
    }
}