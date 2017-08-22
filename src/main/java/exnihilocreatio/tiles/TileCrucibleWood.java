package exnihilocreatio.tiles;

import exnihilocreatio.capabilities.CapabilityHeatManager;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.registries.CrucibleRegistryStone;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileCrucibleWood extends TileCrucibleBase<CrucibleRegistryStone> {

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        ticksSinceLast++;

        if (ticksSinceLast >= 10) {
            ticksSinceLast = 0;

            int heatRate = getHeatRate();

            if (heatRate <= 0)
                return;

            if (solidAmount <= 0) {
                if (!itemHandler.getStackInSlot(0).isEmpty()) {
                    currentItem = new ItemInfo(itemHandler.getStackInSlot(0));
                    itemHandler.getStackInSlot(0).shrink(1);

                    if (itemHandler.getStackInSlot(0).isEmpty()) {
                        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    }

                    solidAmount = CrucibleRegistryStone.getMeltable(currentItem).getAmount();
                } else {
                    if (currentItem != null) {
                        currentItem = null;

                        PacketHandler.sendNBTUpdate(this);
                    }

                    return;
                }
            }

            if (!itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).isItemEqual(currentItem.getItemStack())) {
                // For meltables with a really small "amount"
                while (heatRate > solidAmount && !itemHandler.getStackInSlot(0).isEmpty()) {
                    solidAmount += CrucibleRegistryStone.getMeltable(currentItem).getAmount();
                    itemHandler.getStackInSlot(0).shrink(1);

                    if (itemHandler.getStackInSlot(0).isEmpty()) {
                        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    }
                }
            }

            // Never take more than we have left
            if (heatRate > solidAmount) {
                heatRate = solidAmount;
            }

            if (heatRate > 0 && currentItem != null && CrucibleRegistryStone.canBeMelted(currentItem)) {
                FluidStack toFill = new FluidStack(FluidRegistry.getFluid(CrucibleRegistryStone.getMeltable(currentItem).getFluid()), heatRate);
                int filled = tank.fillInternal(toFill, true);
                solidAmount -= filled;

                if (filled > 0) {
                    PacketHandler.sendNBTUpdate(this);
                }
            }
        }
    }

    public int getHeatRate() {
        BlockPos posBelow = pos.add(0, -1, 0);
        IBlockState stateBelow = getWorld().getBlockState(posBelow);

        if (stateBelow == Blocks.AIR.getDefaultState()) {
            return 0;
        }

        int heat = ExNihiloRegistryManager.HEAT_REGISTRY.getHeatAmount(new BlockInfo(stateBelow));

        if (heat != 0) {
            return heat;
        }

        TileEntity tile = getWorld().getTileEntity(posBelow);

        if (tile != null && tile.hasCapability(CapabilityHeatManager.HEAT_CAPABILITY, EnumFacing.UP)) {
            return tile.getCapability(CapabilityHeatManager.HEAT_CAPABILITY, EnumFacing.UP).getHeatRate();
        }

        return 0;
    }
}
