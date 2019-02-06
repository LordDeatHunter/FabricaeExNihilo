package exnihilocreatio.tiles;

import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public class TileInfestedLeaves extends BaseTileEntity implements ITileLeafBlock {

    @Getter
    private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();


    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void setLeafBlock(IBlockState block) {
        leafBlock = block;
        PacketHandler.sendNBTUpdate(this);
    }

    @Override
    public int getProgress() {
        return 100;
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
            BlockInfo leaves = new BlockInfo(Block.getBlockFromName(tag.getString("leafBlock")), tag.getInteger("leafBlockMeta"));
            if (leaves.isValid())
                leafBlock = leaves.getBlockState();
            else leafBlock = Blocks.LEAVES.getDefaultState();
        } else {
            leafBlock = Blocks.LEAVES.getDefaultState();
        }
    }

    @Override
    public boolean hasFastRenderer() {
        return false;
    }
}
