package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.registries.CrucibleRegistryStone;
import exnihilocreatio.tiles.TileCrucibleWood;
import exnihilocreatio.util.IHasModel;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCrucibleWood extends BlockBase implements IProbeInfoAccessor, IHasModel, ITileEntityProvider {

    public BlockCrucibleWood() {
        super(Material.WOOD, "block_crucible_wood");

        setCreativeTab(ExNihiloCreatio.tabExNihilo);

        this.setHardness(2.0f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileCrucibleWood te = (TileCrucibleWood) world.getTileEntity(pos);

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
        TileCrucibleWood crucible = (TileCrucibleWood) world.getTileEntity(data.getPos());

        if (crucible == null)
            return;

        ItemStack solid = crucible.getCurrentItem() == null ? null : crucible.getCurrentItem().getItemStack();
        FluidStack liquid = crucible.getTank().getFluid();

        String solidName = solid == null ? "None" : solid.getDisplayName();
        String liquidName = liquid == null ? "None" : liquid.getLocalizedName();

        int solidAmount = Math.max(0, crucible.getSolidAmount());

        ItemStack toMelt = crucible.getItemHandler().getStackInSlot(0);

        if (!toMelt.isEmpty()) {
            solidAmount += CrucibleRegistryStone.getMeltable(toMelt).getAmount() * toMelt.getCount();
        }

        probeInfo.text(String.format("Solid (%s): %d", solidName, solidAmount));
        probeInfo.text(String.format("Liquid (%s): %d", liquidName, crucible.getTank().getFluidAmount()));
        probeInfo.text("Rate: " + crucible.getHeatRate() + "x");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileCrucibleWood();
    }
}
