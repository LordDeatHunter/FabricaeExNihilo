package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.recipes.defaults.ExNihilo;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.block.BlockCake;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockEndCake extends BlockCake implements IHasModel {
    public BlockEndCake() {
        super();
        setHardness(0.5F);
        setSoundType(SoundType.CLOTH);
        setRegistryName("block_end_cake");
        setUnlocalizedName("block_end_cake");
        setCreativeTab(ExNihiloCreatio.tabExNihilo);
        Data.BLOCKS.add(this);
    }


    @Override
    public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer playerIn, @Nonnull EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (itemstack.isEmpty()) {
            return this.eatCake(worldIn, pos, state, playerIn);
        } else {

            int bites = state.getValue(BlockCake.BITES);

            if (itemstack.getItem() == Items.ENDER_EYE && bites > 0) {

                if (!worldIn.isRemote){
                    worldIn.setBlockState(pos, state.withProperty(BITES, bites - 1), 3);
                    itemstack.shrink(1);
                }

                return true;
            }
        }

        return false;
    }


    public boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.canEat(false) || player.dimension == 1) {
            return false;
        } else {
            player.addStat(StatList.CAKE_SLICES_EATEN);
            player.getFoodStats().addStats(2, 0.1F);
            int i = state.getValue(BITES);

            if (i < 6) {
                worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
            } else {
                worldIn.setBlockToAir(pos);
            }

            if (!worldIn.isRemote && !player.isRiding()) {
                player.changeDimension(1);
            }

            return true;
        }
    }
}
