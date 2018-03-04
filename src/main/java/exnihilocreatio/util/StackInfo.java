package exnihilocreatio.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public abstract class StackInfo {

    public abstract String toString();

    @Nonnull
    public abstract ItemStack getItemStack();

    public abstract boolean hasBlock();

    @Nonnull
    public abstract Block getBlock();

    @Nonnull
    public abstract IBlockState getBlockState();

    public abstract boolean isValid();

    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);

    public abstract int hashCode();

    /**
     * This is used to check if the contents equals the objects, based on what the object is
     * @param obj The object to check
     * @return Returns true if the output ItemStacks match
     */
    @Override
    public abstract boolean equals(Object obj);
}
