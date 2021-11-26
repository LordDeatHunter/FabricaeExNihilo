package wraith.fabricaeexnihilo.api.crafting;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class Lootable {

    private final ItemStack stack;
    private final List<Double> chances;

    public Lootable(ItemStack stack, List<Double> chances) {
        this.stack = stack;
        this.chances = chances;
    }

    public Lootable(ItemStack stack, double... chances) {
        this.stack = stack;
        this.chances = new ArrayList<>();
        for (var chance : chances) {
            this.chances.add(chance);
        }
    }

    public Lootable(ItemConvertible result, double... chances) {
        this(ItemUtils.asStack(result), chances);
    }

    public Lootable(ItemConvertible result, List<Double> chances) {
        this(ItemUtils.asStack(result), chances);
    }

    public Lootable(Identifier result, List<Double> chances) {
        this(ItemUtils.asStack(Registry.ITEM.get(result)), chances);
    }

    public Lootable(Identifier result, double... chances) {
        this(ItemUtils.asStack(Registry.ITEM.get(result)), chances);
    }

    public boolean isEmpty() {
        return stack.isEmpty() || chances.isEmpty() || chances.contains(0.0);
    }

    public ItemStack getStack() {
        return stack;
    }

    public List<Double> getChances() {
        return chances;
    }

    public static Lootable EMPTY = new Lootable(ItemStack.EMPTY, 0.0);

}
