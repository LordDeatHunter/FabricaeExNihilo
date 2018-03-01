package exnihilocreatio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@AllArgsConstructor
public class BlockInfo {

    public static final BlockInfo EMPTY = new BlockInfo(ItemStack.EMPTY);

    @Getter
    private Block block;

    @Getter
    @Setter
    private int meta;

    public BlockInfo(@Nonnull Block block1) {
        block = block1;
        meta = -1;
    }

    public BlockInfo(@Nonnull IBlockState state) {
        block = state.getBlock();
        meta = state.getBlock().getMetaFromState(state);
    }

    public BlockInfo(@Nonnull ItemStack stack) {
        block = !(stack.getItem() instanceof ItemBlock) ? Blocks.AIR : Block.getBlockFromItem(stack.getItem());
        meta = stack.getItemDamage();
    }

    public BlockInfo(@Nonnull String string) {
        if (string.isEmpty() || string.length() < 2) {
            block = Blocks.AIR;
            meta = 0;
            return;
        }
        String[] split = string.split(":");

        switch (split.length) {
            case 1:
                block = Block.getBlockFromName("minecraft:" + split[0]);
                break;
            case 2:
                try {
                    meta = split[1].equals("*") ? -1 : Integer.parseInt(split[1]);
                    block = Block.getBlockFromName("minecraft:" + split[0]);
                } catch (NumberFormatException e) {
                    meta = -1;
                    block = Block.getBlockFromName(split[0] + ":" + split[1]);
                }
                break;
            case 3:
                try {
                    meta = split[2].equals("*") ? -1 : Integer.parseInt(split[2]);
                    block = Block.getBlockFromName(split[0] + ":" + split[1]);
                } catch (NumberFormatException e) {
                    meta = -1;
                }
                break;
            default:
                block = Blocks.AIR;
                meta = -1;
                break;
        }
    }

    public static BlockInfo readFromNBT(NBTTagCompound tag) {
        Block item_ = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("block")));
        int meta_ = tag.getInteger("meta");

        return new BlockInfo(item_, meta_);
    }

    public String toString() {
        return Block.REGISTRY.getNameForObject(block) + (meta == -1 ? "" : (":" + meta));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("block", Block.REGISTRY.getNameForObject(block).toString());
        tag.setInteger("meta", meta);

        return tag;
    }

    @SuppressWarnings("deprecation")
    public IBlockState getBlockState() {
        return block == null ? null : block.getStateFromMeta(meta == -1 ? 0 : meta);
    }

    public int hashCode() {
        return block == null ? 37 : block.hashCode();
    }

    public boolean equals(Object other) {
        if (other instanceof BlockInfo)
            return ItemStack.areItemStacksEqual(((BlockInfo) other).getItemStack(), getItemStack());
        else if (other instanceof ItemInfo)
            return ItemStack.areItemStacksEqual(((ItemInfo) other).getItemStack(), getItemStack());
        else if (other instanceof ItemStack)
            return ItemStack.areItemStacksEqual(((ItemStack) other), getItemStack());
        else if (other instanceof Block)
            return Block.isEqualTo((Block) other, block);
        else if (other instanceof ItemBlock) {
            return Block.isEqualTo(((ItemBlock) other).getBlock(), block);
        }

        return false;
    }

    public ItemStack getItemStack() {
        Item item = Item.getItemFromBlock(block);
        return new ItemStack(item, 1, meta);
    }
}
