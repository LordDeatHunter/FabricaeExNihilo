package exnihilocreatio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

@AllArgsConstructor
public class ItemInfo implements IStackInfo {

    public static final ItemInfo EMPTY = new ItemInfo(ItemStack.EMPTY);

    @Getter
    @Nonnull
    private Item item;
    @Getter
    @Setter
    private int meta;

    public ItemInfo(@Nonnull Item item) {
        this.item = item;
        meta = -1;
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
            item = Items.AIR;
            meta = -1;
            return;
        }
        String[] split = string.split(":");

        Item item = null;
        int meta = -1;

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
                item = Items.AIR;
                meta = -1;
        }

        if (item == null){
            this.item = Items.AIR;
            this.meta = -1;
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

    public static ItemInfo getItemInfoFromStack(ItemStack stack) {
        return new ItemInfo(stack);
    }

    public static ItemInfo readFromNBT(NBTTagCompound tag) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("item")));
        int meta = tag.getInteger("meta");

        return item == null ? EMPTY : new ItemInfo(item, meta);
    }

    //IStackInfo

    @Override
    public String toString() {
        return Item.REGISTRY.getNameForObject(item) + (meta == -1 ? "" : (":" + meta));
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return new ItemStack(item, 1, meta == -1 ? 0 : meta);
    }

    @Nonnull
    @Override
    public Block getBlock() {
        return Block.getBlockFromItem(item);
    }

    @Nonnull
    @Override
    public IBlockState getBlockState() {
        try {
            return Block.getBlockFromItem(item).getStateFromMeta(meta);
        } catch (Exception e){
            return Block.getBlockFromItem(item).getDefaultState();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("item", Item.REGISTRY.getNameForObject(item) == null ? "" : Item.REGISTRY.getNameForObject(item).toString());
        tag.setInteger("meta", meta);

        return tag;
    }

    @Override
    public boolean isValid() {
        return this != ItemInfo.EMPTY && meta <= Short.MAX_VALUE;
    }

    @Override
    public int hashCode() {
        return this.item.hashCode();
    }

}
