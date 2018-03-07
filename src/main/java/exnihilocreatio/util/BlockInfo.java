package exnihilocreatio.util;

import lombok.AllArgsConstructor;
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
public class BlockInfo extends StackInfo {

    public static final BlockInfo EMPTY = new BlockInfo(ItemStack.EMPTY);

    @Nonnull
    private IBlockState state = Blocks.AIR.getDefaultState();

    public BlockInfo(@Nonnull Block block) {
        setState(block, -1);
    }

    public BlockInfo(@Nonnull Block block, int meta) {
        setState(block, meta);
    }

    public BlockInfo(@Nonnull ItemStack stack) {
        setState(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    public BlockInfo(@Nonnull String string) {
        if (string.isEmpty() || string.length() < 2) {
            this.state = Blocks.AIR.getDefaultState();
            return;
        }
        String[] split = string.split(":");

        Block block;
        int meta = -1;

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
                } catch (NullPointerException e) {
                    block = Blocks.AIR;
                    meta = -1;
                }
                break;
            case 3:
                try {
                    meta = split[2].equals("*") ? -1 : Integer.parseInt(split[2]);
                    block = Block.getBlockFromName(split[0] + ":" + split[1]);
                } catch (NumberFormatException | NullPointerException e) {
                    block = Blocks.AIR;
                    meta = -1;
                }
                break;
            default:
                block = Blocks.AIR;
                meta = -1;
                break;
        }

        if (block == null){
            this.state = Blocks.AIR.getDefaultState();
        }
        else {
            setState(block, meta);
        }
    }

    public void setState(@Nonnull IBlockState state) {
        this.state = state;
    }

    public void setState(@Nonnull Block block, int meta) {
        // This checks for any "all" meta values, before attempting to get the state via meta
        // As it's faster to just grab the default state
        if (meta == 0 || meta == -1 || meta == OreDictionary.WILDCARD_VALUE)
            this.state = block.getDefaultState();

        // This can fail, so in the event it does, we only grab the default state
        else
            try {
                this.state = block.getStateFromMeta(meta);
            } catch (Exception e) {
                this.state = block.getDefaultState();
            }
    }

    public static BlockInfo readFromNBT(NBTTagCompound tag) {
        Block item = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("block")));
        int meta = tag.getInteger("meta");

        return item == null ? EMPTY : new BlockInfo(item, meta);
    }


    //StackInfo

    @Override
    public String toString() {
        int meta = state.getBlock().getMetaFromState(state);
        return Block.REGISTRY.getNameForObject(state.getBlock()) + (meta == -1 ? "" : (":" + meta));
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        Item item = Item.getItemFromBlock(state.getBlock());
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item, 1, state.getBlock().getMetaFromState(state));
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
        return state.getBlock().getMetaFromState(state);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("block", Block.REGISTRY.getNameForObject(state.getBlock()).toString());
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof BlockInfo)
            return ItemStack.areItemStacksEqual(((BlockInfo) other).getItemStack(), getItemStack());
        else if (other instanceof ItemInfo)
            return ItemStack.areItemStacksEqual(((ItemInfo) other).getItemStack(), getItemStack());
        else if (other instanceof ItemStack)
            return ItemStack.areItemStacksEqual(((ItemStack) other), getItemStack());
        else if (other instanceof Block)
            return Block.isEqualTo(state.getBlock(), (Block)other);
        else if (other instanceof ItemBlock) {
            return Block.isEqualTo(state.getBlock(), ((ItemBlock) other).getBlock());
        }
        return false;
    }
}
