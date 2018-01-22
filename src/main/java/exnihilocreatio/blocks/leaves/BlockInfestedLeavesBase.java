package exnihilocreatio.blocks.leaves;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.compatibility.ITOPInfoProvider;
import exnihilocreatio.tiles.TileInfestedLeavesBase;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import exnihilocreatio.util.LogUtil;
import exnihilocreatio.util.Util;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockInfestedLeavesBase extends BlockLeaves implements ITOPInfoProvider, IHasModel, ITileEntityProvider {
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



    public BlockInfestedLeavesBase(){
        Data.BLOCKS.add(this);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false));
        // this.leavesFancy = true;
    }

    /**
     * Infests the leaves at the given blockpos
     * @param world The world this is happening in
     * @param pos The position of the block
     * @param state The state of the block that was there prior
     */
    public static void infestLeafBlock(World world, IBlockState state, BlockPos pos) {
        IBlockState leafState;
        //Prevents a crash with forestry using the new model system

        if (state.getBlock().getRegistryName().getResourceDomain().equalsIgnoreCase("forestry")){
            leafState = Blocks.LEAVES.getDefaultState();
        } else {
            leafState = state;
        }

        world.setBlockState(pos, ((IExtendedBlockState)ModBlocks.infestingLeaves.getDefaultState()).withProperty(BlockInfestedLeavesBase.LEAFBLOCK, leafState), 3);

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileInfestedLeavesBase){
            ((TileInfestedLeavesBase) te).setLeafBlock(leafState);
        } else {
            System.out.println("NO TE HERE: te = " + te);
        }

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

            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileInfestedLeavesBase){
                ((TileInfestedLeavesBase) te).setLeafBlock(leafState);
            }
        }

        else if (Util.isLeaves(block) && !(block.getBlock() instanceof BlockInfestingLeaves)){
            LogUtil.error("Sent leaf change to wrong method, redirecting");
            infestLeafBlock(world, block, pos);
        }
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
        return getDefaultState().withProperty(CHECK_DECAY, (meta & 0b100) != 0)
                .withProperty(DECAYABLE, (meta & 0b010) != 0)
                .withProperty(NEARBYLEAVES, (meta & 0b001) != 0);
    }


    /**
     * 3 bits for data:
     *      0               0                0
     *      checkDecay      decayable       nearbyLeaves
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int checkDecay = state.getValue(CHECK_DECAY) ? 1 : 0;
        int decayable = state.getValue(DECAYABLE) ? 1 : 0;
        int nearbyLeaves = state.getValue(NEARBYLEAVES) ? 1 : 0;

        return checkDecay << 2 | decayable << 1 | nearbyLeaves;
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
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }


    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return Minecraft.isFancyGraphicsEnabled() ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return (this.leavesFancy || blockAccess.getBlockState(pos.offset(side)).getBlock() != this) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos){
        if (state instanceof IExtendedBlockState){
            IBlockState leafState;
            IExtendedBlockState retval = (IExtendedBlockState) state;
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof TileInfestedLeavesBase) {
                leafState = ((TileInfestedLeavesBase)te).getLeafBlock();
            } else {
                System.out.println("gotbasestate ");
                leafState = Blocks.LEAVES.getDefaultState();
            }

            System.out.println("Extended>>leafState = " + leafState);
            return retval.withProperty(LEAFBLOCK, leafState);
        }
        return state;
    }
}
