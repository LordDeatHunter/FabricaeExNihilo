package exnihilocreatio.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public abstract class TileInfestedLeavesBase extends BaseTileEntity {
    private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();


    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void setLeafBlock(IBlockState block) {
        leafBlock = block;
        markDirtyClient();
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tag.setString("leafBlock", leafBlock.getBlock().getRegistryName() == null ? "" : leafBlock.getBlock().getRegistryName().toString());
        tag.setInteger("leafBlockMeta", leafBlock.getBlock().getMetaFromState(leafBlock));
        return tag;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("leafBlock") && tag.hasKey("leafBlockMeta")) {
            try {
                leafBlock = Block.getBlockFromName(tag.getString("leafBlock")).getStateFromMeta(tag.getInteger("leafBlockMeta"));
            } catch (Exception e) {
                leafBlock = Blocks.LEAVES.getDefaultState();
            }
        } else {
            leafBlock = Blocks.LEAVES.getDefaultState();
        }
    }

    @Override
    public boolean hasFastRenderer() {
        return false;
    }

    public IBlockState getLeafBlock(){
        return leafBlock;
    }

}
