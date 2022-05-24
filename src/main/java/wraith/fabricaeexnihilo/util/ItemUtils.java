package wraith.fabricaeexnihilo.util;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ItemUtils {

    public static ItemStack stackWithCount(ItemStack stack, int count) {
        if (count <= 0) return ItemStack.EMPTY;
        var returnStack = stack.copy();
        returnStack.setCount(count);
        return returnStack;
    }

    public static ItemStack asStack(ItemConvertible itemConvertible, int amount) {
        return new ItemStack(itemConvertible.asItem(), amount);
    }

    public static ItemStack asStack(ItemConvertible itemConvertible) {
        return new ItemStack(itemConvertible.asItem());
    }

    public static ItemStack getItemStack(Identifier identifier) {
        return asStack(Registry.ITEM.get(identifier));
    }

    public static ItemStack getExNihiloItemStack(String str) {
        return getItemStack(id(str));
    }

    public static Block getExNihiloBlock(String str) {
        return Registry.BLOCK.get(id(str));
    }

    public static Item getExNihiloItem(String str) {
        return Registry.ITEM.get(id(str));
    }

    public static ItemEntity asEntity(ItemStack stack, World world, double x, double y, double z) {
        return new ItemEntity(world, x, y, z, stack);
    }

    public static ItemEntity asEntity(ItemStack stack, World world, BlockPos pos) {
        return asEntity(stack, world, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void spawnStack(World world, BlockPos pos, ItemStack stack) {
        world.spawnEntity(asEntity(stack, world, pos));
    }

    public static ItemStack ofSize(ItemStack stack, int count) {
        var newStack = stack.copy();
        newStack.setCount(count);
        return newStack;
    }

}
