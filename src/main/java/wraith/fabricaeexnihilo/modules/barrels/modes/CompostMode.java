package wraith.fabricaeexnihilo.modules.barrels.modes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import wraith.fabricaeexnihilo.util.Color;

public class CompostMode implements BarrelMode {

    private double progress = 0;
    private ItemStack result;
    private double amount;
    private Color color;

    public CompostMode(ItemStack result, double amount, Color color) {
        this.result = result;
        this.amount = amount;
        this.color = color;
    }

    @Override
    public String nbtKey() {
        return "compost_mode";
    }

    @Override
    public NbtCompound writeNbt() {
        var nbt = new NbtCompound();
        nbt.put("result", result.writeNbt(new NbtCompound()));
        nbt.putDouble("amount", amount);
        nbt.putInt("color", color.toInt());
        nbt.putDouble("progress", progress);
        return nbt;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static CompostMode fromTag(NbtCompound nbt) {
        var mode = new CompostMode(ItemStack.fromNbt(nbt.getCompound("result")), nbt.getDouble("amount"), new Color(nbt.getInt("color"), false));
        mode.progress = nbt.getDouble("progress");
        return mode;
    }

}