package exnihilocreatio.tiles;

import exnihilocreatio.blocks.leaves.BlockInfestedLeavesBase;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.networking.PacketHandler;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileInfestingLeaves extends TileInfestedLeavesBase implements ITickable {
    @Getter
    private int progress = 0;
    @Getter
    private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();

    private int doProgress = (int) (ModConfig.infested_leaves.ticksToTransform / 100.0);

    @Override
    public void update() {
        if (!world.isRemote) {
            if (doProgress <= 0) {
                progress++;
                if (progress >= 100) {
                    BlockInfestedLeavesBase.setInfested(world, pos, leafBlock);
                    markDirtyClient();
                }

                doProgress = (int) (ModConfig.infested_leaves.ticksToTransform / 100.0);

                //Send packet at the end in case the block gets changed first.
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

    public void setProgress(int newProgress) {
        progress = newProgress;
        PacketHandler.sendNBTUpdate(this);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tag.setInteger("progress", progress);
        return tag;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        progress = tag.getInteger("progress");
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }
}
