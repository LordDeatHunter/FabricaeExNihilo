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
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

@AllArgsConstructor
public class BlockInfo implements IStackInfo {

    public static final BlockInfo EMPTY = new BlockInfo(ItemStack.EMPTY);

    @Getter
    @Nonnull
    private Block block;

    @Getter
    @Setter
    private int meta;

    public BlockInfo(@Nonnull Block block) {
        this.block = block;
        this.meta = -1;
    }

    public BlockInfo(@Nonnull IBlockState state) {
        this.block = state.getBlock();
        this.meta = state.getBlock().getMetaFromState(state);
    }

    public BlockInfo(@Nonnull ItemStack stack) {
        this.block = Block.getBlockFromItem(stack.getItem());
        this.meta = stack.getItemDamage();
    }

    public BlockInfo(@Nonnull String string) {
        if (string.isEmpty() || string.length() < 2) {
            this.block = Blocks.AIR;
            this.meta = -1;
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
            this.block = Blocks.AIR;
            this.meta = -1;
        }
        else {
            this.block = block;
            this.meta = meta;
        }
    }

    public static BlockInfo readFromNBT(NBTTagCompound tag) {
        Block item = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("block")));
        int meta = tag.getInteger("meta");

        return item == null ? EMPTY : new BlockInfo(item, meta);
    }


    //IStackInfo

    @Override
    public String toString() {
        return Block.REGISTRY.getNameForObject(block) + (meta == -1 ? "" : (":" + meta));
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        Item item = Item.getItemFromBlock(block);
        return new ItemStack(item, 1, meta);
    }

    @Nonnull
    @Override
    public IBlockState getBlockState(){
        // This can fail, so in the event it does, we only grab the default state
        try {
            return block.getStateFromMeta(meta);
        } catch (Exception e){
            return block.getDefaultState();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("block", Block.REGISTRY.getNameForObject(block).toString());
        tag.setInteger("meta", meta);

        return tag;
    }

    @Override
    public boolean isValid() {
        return this != BlockInfo.EMPTY && meta <= Short.MAX_VALUE;
    }

    @Override
    public int hashCode() {
        return block.hashCode();
    }

}
