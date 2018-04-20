package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.compatibility.ITOPInfoProvider;
import exnihilocreatio.tiles.TileCrucibleBase;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public abstract class BlockCrucibleBase extends Block implements ITOPInfoProvider, IHasModel {

    public static final PropertyBool THIN = PropertyBool.create("thin");


    public BlockCrucibleBase(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);

        setCreativeTab(ExNihiloCreatio.tabExNihilo);

        Data.BLOCKS.add(this);
    }

    @Override
    public abstract TileEntity createTileEntity(@Nonnull World worldIn, @Nonnull IBlockState state);

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileCrucibleBase te = (TileCrucibleBase) world.getTileEntity(pos);

        if (te != null) {
            IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            return te.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ, fluidHandler);
        } else {
            return true;
        }
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState blockState, IProbeHitData data) {
        TileCrucibleBase crucible = (TileCrucibleBase) world.getTileEntity(data.getPos());

        if (crucible == null)
            return;

        ItemStack solid = crucible.getCurrentItem().getItemStack();
        FluidStack liquid = crucible.getTank().getFluid();

        String solidName = solid == ItemStack.EMPTY ? "None" : solid.getDisplayName();
        String liquidName = liquid == null ? "None" : liquid.getLocalizedName();

        int solidAmount = Math.max(0, crucible.getSolidAmount());

        ItemStack toMelt = crucible.getItemHandler().getStackInSlot(0);

        if (!toMelt.isEmpty()) {
            solidAmount += crucible.getCrucibleRegistry().getMeltable(toMelt).getAmount() * toMelt.getCount();
        }

        probeInfo.text(String.format("Solid (%s): %d", solidName, solidAmount));
        probeInfo.text(String.format("Liquid (%s): %d", liquidName, crucible.getTank().getFluidAmount()));
        probeInfo.text("Rate: " + crucible.getHeatRate() + "x");

    }
}
