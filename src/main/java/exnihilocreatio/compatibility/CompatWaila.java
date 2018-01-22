package exnihilocreatio.compatibility;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.*;
import exnihilocreatio.rotationalPower.IRotationalPowerMember;
import exnihilocreatio.tiles.*;
import mcp.mobius.waila.api.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@WailaPlugin(ExNihiloCreatio.MODID)
public class CompatWaila implements IWailaPlugin, IWailaDataProvider {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(this, BlockBarrel.class);
        registrar.registerBodyProvider(this, BlockSieve.class);
        registrar.registerBodyProvider(this, BlockInfestingLeaves.class);
        registrar.registerBodyProvider(this, BlockCrucibleStone.class);
        registrar.registerBodyProvider(this, BlockCrucibleWood.class);
        registrar.registerBodyProvider(this, BlockStoneAxle.class);
        registrar.registerBodyProvider(this, BlockWaterwheel.class);
        registrar.registerBodyProvider(this, BlockAutoSifter.class);
        registrar.registerBodyProvider(this, BlockSieve.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof BlockBarrel) {
            TileBarrel barrel = (TileBarrel) accessor.getTileEntity();

            if (barrel.getMode() != null) {
                currenttip = barrel.getMode().getWailaTooltip(barrel, currenttip);
            }
        }

        if (accessor.getTileEntity() instanceof TileSieve) {
            TileSieve sieve = (TileSieve) accessor.getTileEntity();

            if (!sieve.getMeshStack().isEmpty()) {
                currenttip.add("Mesh: " + I18n.format(sieve.getMeshStack().getUnlocalizedName() + ".name"));
            } else {
                currenttip.add("Mesh: None");
            }
        }

        if (accessor.getBlock() instanceof BlockInfestingLeaves) {
            if (accessor.getBlock() == ModBlocks.infestingLeaves) {
                TileInfestingLeaves tile = (TileInfestingLeaves) accessor.getTileEntity();

                if (tile.getProgress() >= 100) {
                    currenttip.add("Progress: Done");
                } else {
                    currenttip.add("Progress: " + tile.getProgress() + "%");
                }
            }
            else {
                currenttip.add("Progress: Done");
            }
        }

        if (accessor.getTileEntity() instanceof TileCrucibleBase) {
            TileCrucibleBase tile = (TileCrucibleBase) accessor.getTileEntity();

            ItemStack solid = tile.getCurrentItem() == null ? null : tile.getCurrentItem().getItemStack();
            FluidStack liquid = tile.getTank().getFluid();

            String solidName = solid == null ? "None" : solid.getDisplayName();
            String liquidName = liquid == null ? "None" : liquid.getLocalizedName();

            int solidAmount = Math.max(0, tile.getSolidAmount());

            ItemStack toMelt = tile.getItemHandler().getStackInSlot(0);

            if (!toMelt.isEmpty()) {
                solidAmount += tile.getCrucibleRegistry().getMeltable(toMelt).getAmount() * toMelt.getCount();
            }

            currenttip.add(String.format("Solid (%s): %d", solidName, solidAmount));
            currenttip.add(String.format("Liquid (%s): %d", liquidName, tile.getTank().getFluidAmount()));
            currenttip.add("Rate: " + tile.getHeatRate() + "x");
        }


        if (accessor.getTileEntity() instanceof IRotationalPowerMember) {
            IRotationalPowerMember powerMember = (IRotationalPowerMember) accessor.getTileEntity();

            if (powerMember != null) {
                if (powerMember instanceof TileStoneAxle) {
                    currenttip.add("Facing: " + ((TileStoneAxle) powerMember).facing.getName());
                    currenttip.add("Effective Rotation: " + powerMember.getEffectivePerTickRotation(((TileStoneAxle) powerMember).facing));
                } else if (powerMember instanceof TileWaterwheel) {
                    currenttip.add("Facing: " + (((TileWaterwheel) powerMember).facing.getName()));
                    currenttip.add("Effective Rotation: " + powerMember.getEffectivePerTickRotation(((TileWaterwheel) powerMember).facing));
                }
                currenttip.add("Own Rotation: " + powerMember.getOwnRotation());
            }
        }

        if (accessor.getTileEntity() instanceof TileAutoSifter) {
            TileAutoSifter tileAutoSifter = (TileAutoSifter) accessor.getTileEntity();
            currenttip.add("Stored rotational power: " + tileAutoSifter.storedRotationalPower);
            currenttip.add("Per tick Rotation: " + tileAutoSifter.perTickRotation);
            if (!tileAutoSifter.itemHandlerAutoSifter.getStackInSlot(0).isEmpty())
                currenttip.add("Current Stack: " + tileAutoSifter.itemHandlerAutoSifter.getStackInSlot(0).getCount() + "x " + tileAutoSifter.itemHandlerAutoSifter.getStackInSlot(0).getDisplayName());

        }

        if (accessor.getTileEntity() instanceof TileSieve) {
            TileSieve sieve = (TileSieve) accessor.getTileEntity();
            currenttip.add("Current Progress: " + sieve.getProgress());
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }

}
