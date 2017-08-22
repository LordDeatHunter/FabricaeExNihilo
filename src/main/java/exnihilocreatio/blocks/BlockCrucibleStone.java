package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.registries.CrucibleRegistryStone;
import exnihilocreatio.tiles.TileCrucibleStone;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import exnihilocreatio.util.IHasSpecialRegistry;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockCrucibleStone extends Block implements IProbeInfoAccessor, IHasModel, IHasSpecialRegistry {

    public static final PropertyBool FIRED = PropertyBool.create("fired");

    public BlockCrucibleStone() {
        super(Material.ROCK);
        String name = "block_crucible";
        setUnlocalizedName(name);
        setRegistryName(name);

        setCreativeTab(ExNihiloCreatio.tabExNihilo);

        Data.BLOCKS.add(this);
        this.setHardness(2.0f);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FIRED, false));
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World worldIn, @Nonnull IBlockState state) {
        if (state.getValue(FIRED))
            return new TileCrucibleStone();

        return null;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(FIRED);
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FIRED);
    }

    @Override
    @Nonnull
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FIRED, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FIRED) ? 1 : 0;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(@Nonnull CreativeTabs itemIn, NonNullList<ItemStack> items) {
        if (itemIn == ExNihiloCreatio.tabExNihilo) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos,
                                  EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileCrucibleStone te = (TileCrucibleStone) world.getTileEntity(pos);

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
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent e) {
        ModelResourceLocation unfired = new ModelResourceLocation(getRegistryName(), "fired=false");
        ModelResourceLocation fired = new ModelResourceLocation(getRegistryName(), "fired=true");

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, unfired);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 1, fired);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState blockState, IProbeHitData data) {
        TileCrucibleStone crucible = (TileCrucibleStone) world.getTileEntity(data.getPos());

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

}
