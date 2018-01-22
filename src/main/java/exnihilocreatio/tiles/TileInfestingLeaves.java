package exnihilocreatio.tiles;

import exnihilocreatio.blocks.BlockInfestingLeaves;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.networking.PacketHandler;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileInfestingLeaves extends BaseTileEntity implements ITickable, ITileLeafBlock {
    @Getter
    private int progress = 0;
    @Getter
    private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();

    private int doProgress = (int) (ModConfig.infested_leaves.ticksToTransform / 100.0);
    private int spreadCounter = 0;

    @Override
    public void update() {
        if (!world.isRemote) {
            if (doProgress <= 0) {
                progress++;
                spreadCounter++;
                if (progress >= 100) {
                    BlockInfestingLeaves.setInfested(world, pos, leafBlock);
                    markDirtyClient();
                }

                if (spreadCounter >= ModConfig.infested_leaves.leavesUpdateFrequency) {
                    BlockInfestingLeaves.spread(world, pos, world.getBlockState(pos), world.rand);
                    spreadCounter = 0;
                }


                doProgress = (int) (ModConfig.infested_leaves.ticksToTransform / 100.0);
                //Send packet at the end incase the block gets changed first.
                PacketHandler.sendNBTUpdate(this);
            } else {
                doProgress--;
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void setProgress(int newProgress) {
        progress = newProgress;
        PacketHandler.sendNBTUpdate(this);
    }

    public void setLeafBlock(IBlockState block) {
        leafBlock = block;
        PacketHandler.sendNBTUpdate(this);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tag.setInteger("progress", progress);
        tag.setString("leafBlock", leafBlock.getBlock().getRegistryName() == null ? "" : leafBlock.getBlock().getRegistryName().toString());
        tag.setInteger("leafBlockMeta", leafBlock.getBlock().getMetaFromState(leafBlock));
        return tag;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        progress = tag.getInteger("progress");

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
        return true;
    }
}
