package exnihilocreatio.blocks.leaves;

import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.tools.ICrook;
import exnihilocreatio.tiles.TileInfestingLeaves;
import exnihilocreatio.util.Util;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockInfestingLeaves extends BlockInfestedLeavesBase implements ITileEntityProvider{

    public BlockInfestingLeaves(){
        super();

        this.setUnlocalizedName("block_infesting_leaves");
        this.setRegistryName("block_infesting_leaves");
        this.setDefaultState(getDefaultState().withProperty(NEARBYLEAVES, true));
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {
            if (state.getValue(NEARBYLEAVES)) {
                NonNullList<BlockPos> nearbyLeaves = Util.getNearbyLeaves(world, pos);
                if (nearbyLeaves.isEmpty())
                    world.setBlockState(pos, state.withProperty(NEARBYLEAVES, false), 7);
                else {
                    int progress = ((TileInfestingLeaves) world.getTileEntity(pos)).getProgress();
                    // Delay spreading until 25%
                    if (progress >= ModConfig.infested_leaves.leavesSpreadPercent) {
                        nearbyLeaves.stream().filter(leaves -> rand.nextFloat() <= ModConfig.infested_leaves.leavesSpreadChance).findAny().ifPresent(blockPos -> BlockInfestedLeavesBase.infestLeafBlock(world, world.getBlockState(blockPos), blockPos));
                    }
                }
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    }

    @Override
    public int tickRate(World worldIn) {
        return ModConfig.infested_leaves.leavesUpdateFrequency;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote && Util.isLeaves(worldIn.getBlockState(fromPos)))
            worldIn.setBlockState(pos, state.withProperty(NEARBYLEAVES, true), 7);
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    @Nonnull
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this));
        return ret;
    }

    @Override
    @Nonnull
    public EnumType getWoodType(int meta) {
        return EnumType.OAK;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileInfestingLeaves();
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote && !player.isCreative()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile != null) {
                if (tile instanceof TileInfestingLeaves) {
                    TileInfestingLeaves leaves = (TileInfestingLeaves) tile;

                    if (!player.getHeldItemMainhand().isEmpty()
                            && player.getHeldItemMainhand().getItem() instanceof ICrook) {
                        if (world.rand.nextFloat() < leaves.getProgress() * ModConfig.crooking.stringChance) {
                            Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);
                        }
                    }
                    else if (world.rand.nextFloat() < leaves.getProgress() * ModConfig.crooking.stringChance / 4.0d) {
                        Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);

                    }
                }

                world.removeTileEntity(pos);
            }
        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileInfestingLeaves tile = (TileInfestingLeaves) world.getTileEntity(data.getPos());

        if (tile != null) {
            if (tile.getProgress() >= 100) {
                probeInfo.text("Progress: Done");
            } else {
                probeInfo.progress(tile.getProgress(), 100);
            }
        }

    }

    public static IBlockState getLeafState(IBlockState state){
        if (state instanceof IExtendedBlockState) {
            return ((IExtendedBlockState)state).getValue(BlockInfestingLeaves.LEAFBLOCK);
        }

        return state;
    }
}
