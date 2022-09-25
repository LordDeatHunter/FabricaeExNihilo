package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.modules.barrels.BarrelItemStorage;
import wraith.fabricaeexnihilo.util.CodecUtils;

@SuppressWarnings("UnstableApiUsage")
public class ItemMode extends BarrelMode {
    public static final Codec<ItemMode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    CodecUtils.ITEM_STACK
                            .fieldOf("stack")
                            .forGetter(ItemMode::getStack))
            .apply(instance, ItemMode::new));
    
    private final ItemStack stack;
    
    public ItemMode(ItemStack stack) {
        super();
        this.stack = stack == null ? ItemStack.EMPTY : stack;
    }
    
    @Override
    public String getId() {
        return "item";
    }
    
    @Override
    public BarrelMode copy() {
        return new ItemMode(stack.copy());
    }

    @Override
    public EntryIngredient getReiResult() {
        return EntryIngredients.of(stack);
    }

    public ItemStack getStack() {
        return stack;
    }
    
    @Override
    public long extractItem(ItemVariant item, long maxAmount, TransactionContext transaction, BarrelItemStorage storage) {
        StoragePreconditions.notBlankNotNegative(item, maxAmount);
        if (!ItemVariant.of(stack).equals(item)) return 0;
        
        storage.updateSnapshots(transaction);
        var amount = Math.min(maxAmount, stack.getCount());
        stack.decrement((int) amount);
        if (stack.isEmpty()) storage.barrel.setMode(new EmptyMode());
        return amount;
    }
    
    @Override
    public ItemVariant getItem() {
        return ItemVariant.of(stack);
    }
    
    @Override
    public long getItemAmount() {
        return stack.getCount();
    }
    
    @Override
    public long getItemCapacity() {
        return stack.getMaxCount();
    }
}
