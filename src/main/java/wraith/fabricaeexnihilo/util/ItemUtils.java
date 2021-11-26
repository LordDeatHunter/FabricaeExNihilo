package wraith.fabricaeexnihilo.util;

import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

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

    public static EntryStack<ItemStack> asREIEntry(ItemConvertible itemConvertible) {
        return EntryStacks.of(itemConvertible.asItem());
    }

    public static EntryStack<ItemStack> asREIEntry(ItemStack stack) {
        return EntryStacks.of(stack);
    }

    public static EntryStack<ItemStack> asREIEntry(String item) {
        return asREIEntry(getExNihiloItemStack(item));
    }

    public static EntryStack<FluidStack> asREIEntry(Fluid fluid) {
        return EntryStacks.of(fluid);
    }

    public static ItemStack getItemStack(Identifier identifier) {
        return asStack(Registry.ITEM.get(identifier));
    }

    public static ItemStack getExNihiloItemStack(String str) {
        return getItemStack(FabricaeExNihilo.ID(str));
    }

    public static Block getExNihiloBlock(String str) {
        return Registry.BLOCK.get(FabricaeExNihilo.ID(str));
    }

    public static Item getExNihiloItem(String str) {
        return Registry.ITEM.get(FabricaeExNihilo.ID(str));
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
