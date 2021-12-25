package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelItemStorage;
import wraith.fabricaeexnihilo.recipe.barrel.CompostRecipe;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.Color;

@SuppressWarnings("UnstableApiUsage")
public class CompostMode extends BarrelMode {
    public static final Codec<CompostMode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    CodecUtils.ITEM_STACK
                            .fieldOf("result")
                            .forGetter(CompostMode::getResult),
                    Codec.DOUBLE
                            .fieldOf("amount")
                            .forGetter(CompostMode::getAmount),
                    Codec.DOUBLE
                            .fieldOf("progress")
                            .forGetter(CompostMode::getProgress),
                    Color.CODEC
                            .fieldOf("color")
                            .forGetter(CompostMode::getColor)
                    )
            .apply(instance, CompostMode::new));
    
    private final ItemStack result;
    private double progress = 0;
    private double amount;
    private Color color;
    
    public CompostMode(CompostRecipe recipe) {
        this(recipe.getResult(), recipe.getIncrement(), recipe.getColor());
    }
    
    public CompostMode(ItemStack result, double amount, Color color) {
        this(result, amount, 0, color);
    }
    
    private CompostMode(ItemStack result, double amount, double progress, Color color) {
        this.result = result;
        this.amount = amount;
        this.color = color;
        this.progress = progress;
    }
    
    @Override
    public String getId() {
        return "compost";
    }
    
    @Override
    public BarrelMode copy() {
        return new CompostMode(result.copy(), amount, progress, color);
    }
    
    @Override
    public void tick(BarrelBlockEntity barrel) {
        if (progress >= 1.0) {
            barrel.setMode(new ItemMode(result.copy()));
        } else if (amount >= 1.0) {
            progress += FabricaeExNihilo.CONFIG.modules.barrels.compostRate * barrel.getEfficiencyMultiplier();
            barrel.markDirty();
        }
    }
    
    public double getProgress() {
        return progress;
    }
    
    public ItemStack getResult() {
        return result;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public long insertItem(ItemVariant item, long maxAmount, TransactionContext transaction, BarrelItemStorage storage) {
        var recipe = CompostRecipe.find(item.toStack(), storage.barrel.getWorld());
        storage.updateSnapshots(transaction);
        if (recipe.isPresent() && amount < 1.0 && recipe.get().getResult().isItemEqual(result)) {
            amount = Math.min(amount + recipe.get().getIncrement(), 1.0);
            color = recipe.get().getColor();
            progress = 0;
            return 1;
        }
        return 0;
    }
    
    @Override
    public long getItemCapacity() {
        return 1;
    }
}