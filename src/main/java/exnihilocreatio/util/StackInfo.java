package exnihilocreatio.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface StackInfo {

    String toString();

    @Nonnull
    ItemStack getItemStack();

    boolean hasBlock();

    @Nonnull
    Block getBlock();

    int getMeta();

    @Nonnull
    IBlockState getBlockState();

    boolean isValid();

    NBTTagCompound writeToNBT(NBTTagCompound tag);

    int hashCode();

    /**
     * This is used to check if the contents equals the objects, based on what the object is
     * @param obj The object to check
     * @return Returns true if the output ItemStacks match
     */
    @Override
    boolean equals(Object obj);
}
