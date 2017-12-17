package exnihilocreatio.blocks;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.compatibility.ITOPInfoProvider;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.tools.ICrook;
import exnihilocreatio.tiles.TileInfestedLeaves;
import exnihilocreatio.tiles.TileInfestingLeaves;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import exnihilocreatio.util.LogUtil;
import exnihilocreatio.util.Util;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
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
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockInfestingLeaves extends BlockLeaves implements ITileEntityProvider, ITOPInfoProvider, IHasModel {

    public static PropertyBool NEARBYLEAVES = PropertyBool.create("nearby_leaves");

    public static IUnlistedProperty<IBlockState> LEAFBLOCK = new IUnlistedProperty<IBlockState>() {
        @Override
        public String getName() {
            return "LeafBlock";
        }

        @Override
        public boolean isValid(IBlockState value) {
            return true;
        }

        @Override
        public Class<IBlockState> getType() {
            return IBlockState.class;
        }

        @Override
        public String valueToString(IBlockState value) {
            return value.toString();
        }
    };

    public BlockInfestingLeaves(){
        this(InfestedType.INFESTING);
        this.setUnlocalizedName("block_infesting_leaves");
        this.setRegistryName("block_infesting_leaves");
        Data.BLOCKS.add(this);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false).withProperty(NEARBYLEAVES, true));
    }

    protected BlockInfestingLeaves(InfestedType type) {
        super();
        this.setTickRandomly(true);
        this.leavesFancy = true;
    }

    public static void infestLeafBlock(World world, IBlockState state, BlockPos pos) {
        IBlockState leafState;
        //Prevents a crash with forestry using the new model system
        if (Block.REGISTRY.getNameForObject(state.getBlock()).getResourceDomain().equalsIgnoreCase("forestry"))
            leafState = Blocks.LEAVES.getDefaultState();
        else leafState = state;
        world.setBlockState(pos, ModBlocks.infestingLeaves.getDefaultState(), 3);
        ((TileInfestingLeaves) world.getTileEntity(pos)).setLeafBlock(leafState);
    }

    /**
     * Used to set an existing infesting leaves block to infested
     * @param world The world this is happening in
     * @param pos The position of the block
     * @param leafState The leaf state captured by the infesting leaves block block
     */
    public static void setInfested(World world, BlockPos pos, IBlockState leafState) {
        IBlockState block = world.getBlockState(pos);
        if (block.getBlock() instanceof BlockInfestingLeaves) {
            IExtendedBlockState retval = (IExtendedBlockState) ModBlocks.infestedLeaves.getDefaultState();
            world.setBlockState(pos, retval.withProperty(BlockInfestedLeaves.LEAFBLOCK, leafState), 7);
            ((TileInfestedLeaves)world.getTileEntity(pos)).setLeafBlock(leafState);
        }
        else if (Util.isLeaves(block) && !(block.getBlock() instanceof BlockInfestedLeaves)){
            LogUtil.error("Sent leaf change to wrong method, redirecting");
            infestLeafBlock(world, block, pos);
        }
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
                        nearbyLeaves.stream().filter(leaves -> rand.nextFloat() <= ModConfig.infested_leaves.leavesSpreadChance).findAny().ifPresent(blockPos -> BlockInfestingLeaves.infestLeafBlock(world, world.getBlockState(blockPos), blockPos));
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
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos){
        if (state instanceof IExtendedBlockState){
            IExtendedBlockState retval = (IExtendedBlockState) state;
            IBlockState leafState;
            if (world.getTileEntity(pos) != null) {
                leafState = ((TileInfestingLeaves) world.getTileEntity(pos)).getLeafBlock();
            }
            else leafState = Blocks.LEAVES.getDefaultState();
            return retval.withProperty(LEAFBLOCK, leafState);
        }
        return state;
    }
    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        IProperty<?>[] listedProperties = {CHECK_DECAY, DECAYABLE, NEARBYLEAVES};
        IUnlistedProperty<?>[] unlistedProperties = {LEAFBLOCK};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    @Nonnull
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CHECK_DECAY, meta == 0 || meta == 1 || meta == 3)
                .withProperty(DECAYABLE, meta == 0 || meta == 2 || meta == 4)
                .withProperty(NEARBYLEAVES, meta == 0 || meta == 1 || meta == 4);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        boolean checkDecay = state.getValue(CHECK_DECAY);
        boolean decayable = state.getValue(DECAYABLE);
        boolean nearbyLeaves = state.getValue(NEARBYLEAVES);
        if (checkDecay && decayable && nearbyLeaves)
            return 0;
        if (checkDecay)
            return nearbyLeaves ? 1 : 3;
        if (decayable)
            return nearbyLeaves ? 2 : 4;
        return 5;
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
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState blockState, IProbeHitData data) {
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
        if (state instanceof IExtendedBlockState){
            return ((IExtendedBlockState)state).getValue(BlockInfestedLeaves.LEAFBLOCK);
        }
        return state;
    }

    enum InfestedType{
        INFESTING,
        INFESTED
    }
}
