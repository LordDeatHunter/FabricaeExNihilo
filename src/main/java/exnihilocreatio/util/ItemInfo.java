package exnihilocreatio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

@AllArgsConstructor
public class ItemInfo extends StackInfo {

    public static final ItemInfo EMPTY = new ItemInfo(ItemStack.EMPTY);

    @Getter
    @Nonnull
    private Item item;
    @Getter
    @Setter
    private int meta = 0;

    public ItemInfo(@Nonnull Item item) {
        this.item = item;
    }

    public ItemInfo(@Nonnull ItemStack stack) {
        this.item = stack.getItem();
        this.meta = stack.getItemDamage();
    }

    public ItemInfo(@Nonnull Block block, int blockMeta) {
        this.item = Item.getItemFromBlock(block);
        this.meta = block == Blocks.AIR ? 0 : blockMeta;
    }

    public ItemInfo(@Nonnull String string) {
        if (string.isEmpty() || string.length() < 2) {
            item = Items.AIR;
            return;
        }
        String[] split = string.split(":");

        Item item = null;
        int meta = 0;

        switch (split.length) {
            case 1:
                item = Item.getByNameOrId("minecraft:" + split[0]);
                break;
            case 2:
                try {
                    meta = split[1].equals("*") ? 0 : Integer.parseInt(split[1]);
                    item = Item.getByNameOrId("minecraft:" + split[0]);
                } catch (NumberFormatException e) {
                    meta = 0;
                    item = Item.getByNameOrId(split[0] + ":" + split[1]);
                }
                break;
            case 3:
                try {
                    meta = split[2].equals("*") ? 0 : Integer.parseInt(split[2]);
                    item = Item.getByNameOrId(split[0] + ":" + split[1]);
                } catch (NumberFormatException e) {
                    meta = 0;
                }
                break;
            default:
                this.item = Items.AIR;
                return;
        }

        if (item == null){
            this.item = Items.AIR;
        }
        else {
            this.item = item;
            this.meta = meta;
        }
    }

    public ItemInfo(@Nonnull IBlockState state) {
        item = Item.getItemFromBlock(state.getBlock());
        meta = state.getBlock().getMetaFromState(state);
    }

    public static ItemInfo readFromNBT(NBTTagCompound tag) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("item")));
        int meta = tag.getInteger("meta");

        return item == null ? EMPTY : new ItemInfo(item, meta);
    }

    //StackInfo

    @Override
    public String toString() {
        return ForgeRegistries.ITEMS.getKey(item) + (meta == 0 ? "" : (":" + meta));
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item, 1, meta);
    }

    @Override
    public boolean hasBlock() {
        return item instanceof ItemBlock;
    }

    @Nonnull
    @Override
    public Block getBlock() {
        return Block.getBlockFromItem(item);
    }

    @Nonnull
    @Override
    public IBlockState getBlockState() {
        if (item == Items.AIR)
            return Blocks.AIR.getDefaultState();
        try {
            return Block.getBlockFromItem(item).getStateFromMeta(meta);
        } catch (Exception e){
            return Block.getBlockFromItem(item).getDefaultState();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("item", ForgeRegistries.ITEMS.getKey(item) == null ? "" : ForgeRegistries.ITEMS.getKey(item).toString());
        tag.setInteger("meta", meta);

        return tag;
    }

    @Override
    public boolean isValid() {
        return this.item != Items.AIR && meta <= OreDictionary.WILDCARD_VALUE;
    }

    @Override
    public int hashCode() {
        return this.item.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
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
