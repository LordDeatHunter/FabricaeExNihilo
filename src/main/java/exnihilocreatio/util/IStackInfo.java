package exnihilocreatio.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface IStackInfo {

    String toString();

    @Nonnull
    ItemStack getItemStack();

    @Nonnull
    Block getBlock();

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
    default boolean matches(Object obj) {
        if (obj instanceof ItemInfo)
            return ItemStack.areItemStacksEqual(((ItemInfo) obj).getItemStack(), getItemStack());
        else if (obj instanceof ItemStack)
            return ItemStack.areItemStacksEqual((ItemStack) obj, getItemStack());
        else if (obj instanceof BlockInfo)
            return ItemStack.areItemStacksEqual(((BlockInfo) obj).getItemStack(), getItemStack());
        else if (obj instanceof Block){
            BlockInfo block = new BlockInfo((Block)obj);
            return ItemStack.areItemStacksEqual(block.getItemStack(), getItemStack());
        }
        else if (obj instanceof Item){
            ItemInfo item = new ItemInfo((Item)obj);
            return ItemStack.areItemStacksEqual(item.getItemStack(), getItemStack());
        }
        return false;
    }
}
