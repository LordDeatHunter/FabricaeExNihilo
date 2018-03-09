package exnihilocreatio.util;

import lombok.Getter;
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

public class BlockInfo extends StackInfo {

    public static final BlockInfo EMPTY = new BlockInfo(ItemStack.EMPTY);

    @Nonnull
    private IBlockState state;

    @Getter
    private boolean isWildcard = false;

    public BlockInfo(@Nonnull Block block) {
        this.state = getStateFromMeta(block, 0);
        this.isWildcard = true;
    }

    public BlockInfo(@Nonnull Block block, int meta) {
        this.state = getStateFromMeta(block, meta);
        if (meta == -1 || meta == OreDictionary.WILDCARD_VALUE)
            this.isWildcard = true;
    }

    public BlockInfo(@Nonnull IBlockState state){
        this.state = state;
    }

    public BlockInfo(@Nonnull ItemStack stack) {
        this.state = getStateFromMeta(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    public BlockInfo(@Nonnull String string) {
        if (string.isEmpty() || string.length() < 2) {
            this.state = Blocks.AIR.getDefaultState();
            this.isWildcard = true;
            return;
        }
        String[] split = string.split(":");

        Block block;
        int meta = 0;

        switch (split.length) {
            case 1:
                block = Block.getBlockFromName("minecraft:" + split[0]);
                break;
            case 2:
                try {
                    meta = split[1].equals("*") ? -1 : Integer.parseInt(split[1]);
                    block = Block.getBlockFromName("minecraft:" + split[0]);
                } catch (NumberFormatException e) {
                    meta = 0;
                    block = Block.getBlockFromName(split[0] + ":" + split[1]);
                } catch (NullPointerException e) {
                    this.state = Blocks.AIR.getDefaultState();
                    this.isWildcard = true;
                    return;
                }
                break;
            case 3:
                try {
                    meta = split[2].equals("*") ? -1 : Integer.parseInt(split[2]);
                    block = Block.getBlockFromName(split[0] + ":" + split[1]);
                } catch (NumberFormatException | NullPointerException e) {
                    this.state = Blocks.AIR.getDefaultState();
                    this.isWildcard = true;
                    return;
                }
                break;
            default:
                this.state = Blocks.AIR.getDefaultState();
                this.isWildcard = true;
                return;
        }

        if (block == null){
            this.state = Blocks.AIR.getDefaultState();
        }
        else {
            if (meta == -1 || meta == OreDictionary.WILDCARD_VALUE) {
                this.state = getStateFromMeta(block, 0);
                this.isWildcard = true;
            }
            else
                this.state = getStateFromMeta(block, meta);
        }
    }

    public static BlockInfo readFromNBT(NBTTagCompound tag) {
        Block item = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("block")));
        int meta = tag.getInteger("meta");

        return item == null ? EMPTY : new BlockInfo(item, meta);
    }

    /**
     * This is a safe version of the block's getStateFromMeta
     * As it will attempt to get the meta state, but will
     * catch any errors and return a default state instead.
     */
    @Nonnull
    public static IBlockState getStateFromMeta(Block block, int meta){
        try {
            return block.getStateFromMeta(meta);
        } catch (Exception e) {
            return block.getDefaultState();
        }
    }


    //StackInfo

    @Override
    public String toString() {
        int meta = getMeta();
        return ForgeRegistries.BLOCKS.getKey(state.getBlock()) + (meta == 0 ? "" : (":" + meta));
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        Item item = Item.getItemFromBlock(state.getBlock());
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item, 1, getMeta());
    }

    @Override
    public boolean hasBlock() {
        return true;
    }

    @Nonnull
    @Override
    public Block getBlock() {
        return state.getBlock();
    }

    @Nonnull
    @Override
    public IBlockState getBlockState() {
        return state;
    }

    @Override
    public int getMeta() {
        return isWildcard ? -1 : state.getBlock().getMetaFromState(state);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("block", ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString());
        tag.setInteger("meta", state.getBlock().getMetaFromState(state));

        return tag;
    }

    @Override
    public boolean isValid() {
        return this.state != Blocks.AIR.getDefaultState();
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    /**
     * Equals will vary base on Wildcard value. If wildcard is true
     * this will only compare blocks, otherwise it will also factor
     * in meta values.
     */
    @Override
    public boolean equals(Object other) {
        if (isWildcard){
            if (other instanceof BlockInfo)
                return Block.isEqualTo(state.getBlock(), ((BlockInfo) other).getBlock());
            else if (other instanceof ItemInfo)
                return Block.isEqualTo(state.getBlock(), ((ItemInfo) other).getBlock());
            else if (other instanceof ItemStack)
                return Block.isEqualTo(state.getBlock(), Block.getBlockFromItem(((ItemStack) other).getItem()));
            else if (other instanceof Block)
                return Block.isEqualTo(state.getBlock(), (Block)other);
            else if (other instanceof ItemBlock) {
                return Block.isEqualTo(state.getBlock(), ((ItemBlock) other).getBlock());
            }
        }
        else {
            if (other instanceof BlockInfo)
                return state == ((BlockInfo) other).state;
            else if (other instanceof ItemInfo)
                return state == ((ItemInfo) other).getBlockState();
            else if (other instanceof ItemStack)
                return state == BlockInfo.getStateFromMeta(Block.getBlockFromItem(((ItemStack) other).getItem()), ((ItemStack) other).getItemDamage());
            else if (other instanceof Block)
                return Block.isEqualTo(state.getBlock(), (Block)other);
            else if (other instanceof ItemBlock) {
                return Block.isEqualTo(state.getBlock(), ((ItemBlock) other).getBlock());
            }
        }

        return false;
    }
}
