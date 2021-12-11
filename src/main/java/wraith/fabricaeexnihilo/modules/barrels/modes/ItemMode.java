package wraith.fabricaeexnihilo.modules.barrels.modes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemMode implements BarrelMode {

    private ItemStack stack;

    public ItemMode(ItemStack stack) {
        this.stack = stack == null ? ItemStack.EMPTY : stack;
    }

    @Override
    public NbtCompound writeNbt() {
        var nbt = new NbtCompound();
        nbt.put("item_mode", stack.writeNbt(new NbtCompound()));
        return nbt;
    }

    @Override
    public String nbtKey() {
        return "item_mode";
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public static ItemMode readNbt(NbtCompound nbt) {
        return new ItemMode(ItemStack.fromNbt(nbt.contains("item_mode") ? nbt.getCompound("item_mode") : nbt));
    }

}
