package exnihilocreatio.blocks;

import exnihilocreatio.ModBlocks;
import exnihilocreatio.config.Config;
import exnihilocreatio.items.tools.ICrook;
import exnihilocreatio.tiles.TileInfestedLeaves;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import exnihilocreatio.util.Util;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockInfestedLeaves extends BlockLeaves implements ITileEntityProvider, IProbeInfoAccessor, IHasModel {
    int[] surroundings;

    public BlockInfestedLeaves() {
        super();
        this.setUnlocalizedName("block_infested_leaves");
        this.setRegistryName("block_infested_leaves");
        Data.BLOCKS.add(this);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true));
        this.leavesFancy = true;
    }

    public static void infestLeafBlock(World world, BlockPos pos) {
        IBlockState block = world.getBlockState(pos);

        if (block.getBlock().isLeaves(block, world, pos) && !block.getBlock().equals(ModBlocks.infestedLeaves)) {
            world.setBlockState(pos, ModBlocks.infestedLeaves.getDefaultState());

            TileInfestedLeaves tile = (TileInfestedLeaves) world.getTileEntity(pos);

            if (tile != null) {
                tile.setLeafBlock(block);
            }
        }
    }

    @Override
    @Nonnull
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    @Nonnull
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CHECK_DECAY, meta == 0 || meta == 1).withProperty(DECAYABLE, meta == 0 || meta == 2);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        boolean checkDecay = state.getValue(CHECK_DECAY);
        boolean decayable = state.getValue(DECAYABLE);
        if (checkDecay && decayable)
            return 0;
        if (checkDecay)
            return 1;
        if (decayable)
            return 2;
        return 3;
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public void updateTick(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
        if (!worldIn.isRemote)
        {
            if (state.getValue(CHECK_DECAY) && state.getValue(DECAYABLE))
            {
                int posX = pos.getX();
                int posY = pos.getY();
                int posZ = pos.getZ();

                if (this.surroundings == null)
                {
                    this.surroundings = new int[32768];
                }

                if (worldIn.isAreaLoaded(new BlockPos(posX - 5, posY - 5, posZ - 5), new BlockPos(posX + 5, posY + 5, posZ + 5)))
                {
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                    for (int i2 = -4; i2 <= 4; ++i2)
                    {
                        for (int j2 = -4; j2 <= 4; ++j2)
                        {
                            for (int k2 = -4; k2 <= 4; ++k2)
                            {
                                IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(posX + i2, posY + j2, posZ + k2));
                                Block block = iblockstate.getBlock();

                                if (!block.canSustainLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(posX + i2, posY + j2, posZ + k2)))
                                {
                                    if (block.isLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(posX + i2, posY + j2, posZ + k2)))
                                    {
                                        this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -2;
                                    }
                                    else
                                    {
                                        this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -1;
                                    }
                                }
                                else
                                {
                                    this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = 0;
                                }
                            }
                        }
                    }

                    for (int i3 = 1; i3 <= 4; ++i3)
                    {
                        for (int j3 = -4; j3 <= 4; ++j3)
                        {
                            for (int k3 = -4; k3 <= 4; ++k3)
                            {
                                for (int l3 = -4; l3 <= 4; ++l3)
                                {
                                    if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16] == i3 - 1)
                                    {
                                        if (this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
                                        {
                                            this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
                                        }

                                        if (this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
                                        {
                                            this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
                                        }

                                        if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] == -2)
                                        {
                                            this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] = i3;
                                        }

                                        if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] == -2)
                                        {
                                            this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] = i3;
                                        }

                                        if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] == -2)
                                        {
                                            this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] = i3;
                                        }

                                        if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] == -2)
                                        {
                                            this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] = i3;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int l2 = this.surroundings[16912];

                if (l2 < 0) {
                    this.destroy(worldIn, pos);
                }
            }
        }
    }

    @Override
    public void destroy(World worldIn, @Nonnull BlockPos pos) {
        if (worldIn.rand.nextInt(3) == 0){
            TileEntity te = worldIn.getTileEntity(pos);
            if (te != null && te instanceof TileInfestedLeaves){
                TileInfestedLeaves infestedLeaves = (TileInfestedLeaves) te;
                System.out.println("infestedLeaves.getProgress() = " + infestedLeaves.getProgress());
                if (worldIn.rand.nextFloat() < infestedLeaves.getProgress() * Config.stringChance / 4.0d){
                    spawnAsEntity(worldIn, pos, new ItemStack(Items.STRING));
                }
            }
        }
        super.destroy(worldIn, pos);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote && !player.isCreative()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile != null) {
                if (tile instanceof TileInfestedLeaves) {
                    TileInfestedLeaves leaves = (TileInfestedLeaves) tile;

                    if (!player.getHeldItemMainhand().isEmpty()
                            && player.getHeldItemMainhand().getItem() instanceof ICrook) {
                        if (world.rand.nextFloat() < leaves.getProgress() * Config.stringChance) {
                            Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);
                        }

                        if (world.rand.nextFloat() < leaves.getProgress() * Config.stringChance / 4.0d) {
                            Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);
                        }
                    }
                }

                world.removeTileEntity(pos);
            }
        }
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
        return new TileInfestedLeaves();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState blockState, IProbeHitData data) {

        TileInfestedLeaves tile = (TileInfestedLeaves) world.getTileEntity(data.getPos());

        if (tile != null) {
            if (tile.getProgress() >= 1.0F) {
                probeInfo.text("Progress: Done");
            } else {
                probeInfo.progress((int) (tile.getProgress() * 100), 100);
            }
        }
    }
}
