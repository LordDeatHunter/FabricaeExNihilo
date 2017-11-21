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
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class TileInfestedLeaves extends BaseTileEntity implements ITickable {

    private static int tileId = 0;

    @Getter
    private boolean hasNearbyLeaves = true;
    @Getter
    private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();

    // Stop ALL infested leaves from updating on the same tick always - this way they're evenly spread out and not causing a spike in tick time every time they update
    // Let's hope no one gets 2 billion in their server
    private int updateIndex = tileId++ % ModConfig.infested_leaves.leavesUpdateFrequency;

    @Override
    public void update() {
        if (!getWorld().isRemote && (hasNearbyLeaves || getWorld().getTotalWorldTime() % ModConfig.infested_leaves.leavesUpdateFrequency == updateIndex)) {
            hasNearbyLeaves = false;

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos newPos = new BlockPos(pos.add(x, y, z));
                        IBlockState state = getWorld().getBlockState(newPos);

                        if (state != Blocks.AIR.getDefaultState() && state.getBlock() != Blocks.AIR && state.getBlock() != ModBlocks.infestedLeaves) {
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

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void setLeafBlock(IBlockState block) {
        leafBlock = block;
        PacketHandler.sendNBTUpdate(this);
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
}
