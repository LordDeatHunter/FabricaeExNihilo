package wraith.fabricaeexnihilo.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;

public class CopyEnchantmentsLootFunction extends ConditionalLootFunction {
    public static final LootFunctionType TYPE = new LootFunctionType(new CopyEnchantmentsLootFunction.Serializer());
    
    protected CopyEnchantmentsLootFunction(LootCondition[] conditions) {
        super(conditions);
    }
    
    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (context.get(LootContextParameters.BLOCK_ENTITY) instanceof EnchantableBlockEntity enchantable && enchantable.getEnchantmentContainer().getEnchantments().size() > 0) {
            EnchantmentContainer.addEnchantments(stack, enchantable.getEnchantmentContainer());
        }
        return stack;
    }
    
    @Override
    public LootFunctionType getType() {
        return TYPE;
    }

    public static Builder builder() {
        return new Builder();
    }

    protected static class Builder extends ConditionalLootFunction.Builder<CopyEnchantmentsLootFunction.Builder> {
        @Override
        protected Builder getThisBuilder() {
            return this;
        }

        @Override
        public LootFunction build() {
            return new CopyEnchantmentsLootFunction(getConditions());
        }
    }

    private static class Serializer extends ConditionalLootFunction.Serializer<CopyEnchantmentsLootFunction> {
        @Override
        public CopyEnchantmentsLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new CopyEnchantmentsLootFunction(conditions);
        }
    
        @Override
        public void toJson(JsonObject json, CopyEnchantmentsLootFunction object, JsonSerializationContext context) {
            super.toJson(json, object, context);
        }
    }
}
