package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.tiles.TileInfestedLeaves;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.Util;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockInfestedLeaves extends BlockInfestingLeaves {

    public BlockInfestedLeaves() {
        super(InfestedType.INFESTED);
        this.setTranslationKey("block_infested_leaves");
        this.setRegistryName("block_infested_leaves");
        this.setCreativeTab(ExNihiloCreatio.tabExNihilo);
        Data.BLOCKS.add(this);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void randomTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        this.updateTick(world, pos, state, rand);
        if (!world.isRemote) {
            if (state.getValue(NEARBYLEAVES)) {
                NonNullList<BlockPos> nearbyLeaves = Util.getNearbyLeaves(world, pos);
                if (nearbyLeaves.isEmpty()) {
                    world.setBlockState(pos, state.withProperty(NEARBYLEAVES, false), 7);
                } else {
                    nearbyLeaves.stream().filter(leaves -> rand.nextFloat() <= ModConfig.infested_leaves.leavesSpreadChanceFloat).findAny().ifPresent(blockPos -> BlockInfestingLeaves.infestLeafBlock(world, world.getBlockState(blockPos), blockPos));
                }
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote && Util.isLeaves(worldIn.getBlockState(fromPos)))
            worldIn.setBlockState(pos, state.withProperty(NEARBYLEAVES, true), 7);
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileInfestedLeaves();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState blockState, IProbeHitData data) {
        probeInfo.text("Progress: Done");
    }


}
