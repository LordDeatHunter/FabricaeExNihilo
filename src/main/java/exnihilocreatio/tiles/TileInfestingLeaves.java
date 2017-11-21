package exnihilocreatio.tiles;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockInfestingLeaves;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.util.Util;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class TileInfestingLeaves extends BaseTileEntity implements ITickable {
    private static int tileId = 0;

    @Getter
    private int progress = 0;
    @Getter
    private boolean hasNearbyLeaves = true;
    @Getter
    private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();

    // Stop ALL infested leaves from updating on the same tick always - this way they're evenly spread out and not causing a spike in tick time every time they update
    // Let's hope no one gets 2 billion in their server
    private int updateIndex = tileId++ % ModConfig.infested_leaves.leavesUpdateFrequency;

    private int doProgress = (int) (ModConfig.infested_leaves.ticksToTransform / 100.0);

    @Override
    public void update() {
        if (!world.isRemote) {
            if (doProgress <= 0) {
                // Only update everytime 1% of progress is done
                progress++;
                if (progress >= 100) {
                    BlockInfestingLeaves.setInfested(world, pos, leafBlock);
                } else
                    PacketHandler.sendNBTUpdate(this);

                doProgress = (int) (ModConfig.infested_leaves.ticksToTransform / 100.0);
            } else {
                doProgress--;
            }


            // Don't update unless there's leaves nearby, or we haven't checked for leavesUpdateFrequency ticks. And only update on the server
            // Delay spreading until 25%
            if (progress >= 25) {
                if (hasNearbyLeaves || getWorld().getTotalWorldTime() % ModConfig.infested_leaves.leavesUpdateFrequency == updateIndex) {
                    hasNearbyLeaves = false;

                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                BlockPos newPos = new BlockPos(pos.add(x, y, z));
                                IBlockState state = getWorld().getBlockState(newPos);

                                if (state != Blocks.AIR.getDefaultState() && state.getBlock() != Blocks.AIR && state.getBlock() != ModBlocks.infestingLeaves) {
                                    ItemStack itemStack = new ItemStack(state.getBlock());
                                    if (OreDictionary.getOres("treeLeaves").stream().anyMatch(stack1 -> Util.compareItemStack(stack1, itemStack))) {
                                        hasNearbyLeaves = true;

                                        if (getWorld().rand.nextFloat() < ModConfig.infested_leaves.leavesSpreadChance) {
                                            BlockInfestingLeaves.infestLeafBlock(getWorld(), newPos);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
