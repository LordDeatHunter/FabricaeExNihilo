package wraith.fabricaeexnihilo.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ItemUtils {
    public static ItemStack asStack(ItemConvertible itemConvertible, int amount) {
        return new ItemStack(itemConvertible.asItem(), amount);
    }

    public static ItemStack asStack(ItemConvertible itemConvertible) {
        return new ItemStack(itemConvertible.asItem());
    }

    public static Block getExNihiloBlock(String str) {
        return Registry.BLOCK.get(id(str));
    }

    public static Item getExNihiloItem(String str) {
        return getItem(id(str));
    }

    public static ItemStack getExNihiloItemStack(String str) {
        return getItemStack(id(str));
    }

    public static Item getItem(Identifier identifier) {
        return Registry.ITEM.get(identifier);
    }

    public static ItemStack getItemStack(Identifier identifier) {
        return asStack(getItem(identifier));
    }

    public static ItemStack ofSize(ItemStack stack, int count) {
        var newStack = stack.copy();
        newStack.setCount(count);
        return newStack;
    }
}
