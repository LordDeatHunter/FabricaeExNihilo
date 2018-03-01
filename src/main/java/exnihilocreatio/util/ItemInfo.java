package exnihilocreatio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@AllArgsConstructor
public class ItemInfo {

    public static final ItemInfo EMPTY = new ItemInfo(ItemStack.EMPTY);

    @Getter
    @Nonnull
    private Item item;
    @Getter
    @Setter
    private int meta;

    public ItemInfo(@Nonnull Item item) {
        this.item = item;
        meta = 0;
    }

    public ItemInfo(@Nonnull ItemStack stack) {
        item = stack.getItem();
        meta = stack.getMetadata();
    }

    public ItemInfo(@Nonnull Block block, int blockMeta) {
        item = Item.getItemFromBlock(block);
        meta = block == Blocks.AIR ? -1 : blockMeta;
    }

    public ItemInfo(@Nonnull String string) {
        if (string.isEmpty() || string.length() < 2) {
            item = ItemStack.EMPTY.getItem();
            meta = ItemStack.EMPTY.getMetadata();
            return;
        }
        String[] split = string.split(":");

        switch (split.length) {
            case 1:
                item = Item.getByNameOrId("minecraft:" + split[0]);
                break;
            case 2:
                try {
                    meta = split[1].equals("*") ? -1 : Integer.parseInt(split[1]);
                    item = Item.getByNameOrId("minecraft:" + split[0]);
                } catch (NumberFormatException e) {
                    meta = -1;
                    item = Item.getByNameOrId(split[0] + ":" + split[1]);
                }
                break;
            case 3:
                try {
                    meta = split[2].equals("*") ? -1 : Integer.parseInt(split[2]);
                    item = Item.getByNameOrId(split[0] + ":" + split[1]);
                } catch (NumberFormatException e) {
                    meta = -1;
                }
                break;
            default:
                item = ItemStack.EMPTY.getItem();
                meta = ItemStack.EMPTY.getMetadata();
        }
    }

    public ItemInfo(@Nonnull IBlockState state) {
        item = Item.getItemFromBlock(state.getBlock());
        meta = state.getBlock().getMetaFromState(state);
    }

    public static ItemInfo getItemInfoFromStack(ItemStack stack) {
        return new ItemInfo(stack);
    }

    public static ItemInfo readFromNBT(NBTTagCompound tag) {
        Item item_ = Item.REGISTRY.getObject(new ResourceLocation(tag.getString("item")));
        int meta_ = tag.getInteger("meta");

        return new ItemInfo(item_, meta_);
    }

    public String toString() {
        return Item.REGISTRY.getNameForObject(item) + (meta == -1 ? "" : (":" + meta));
    }

    public ItemStack getItemStack() {
        return new ItemStack(item, 1, meta == -1 ? 0 : meta);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("item", Item.REGISTRY.getNameForObject(item) == null ? "" : Item.REGISTRY.getNameForObject(item).toString());
        tag.setInteger("meta", meta);

        return tag;
    }

    public boolean isValid() {
        return meta <= Short.MAX_VALUE && item != ItemStack.EMPTY.getItem();
    }

    public int hashCode() {
        return item.hashCode();
    }

    public boolean equals(Object other) {
        if (other instanceof ItemInfo)
            return ItemStack.areItemStacksEqual(((ItemInfo) other).getItemStack(), getItemStack());
        else if (other instanceof ItemStack)
            return ItemStack.areItemStacksEqual((ItemStack) other, getItemStack());
        else if (other instanceof BlockInfo)
            return ItemStack.areItemStacksEqual(((BlockInfo) other).getItemStack(), getItemStack());

        return false;
    }
}
