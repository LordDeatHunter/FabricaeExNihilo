package exnihilocreatio.tiles;

import exnihilocreatio.config.ModConfig;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileCrucibleWood extends TileCrucibleBase {

    public TileCrucibleWood() {
        super(ExNihiloRegistryManager.CRUCIBLE_WOOD_REGISTRY);
    }

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

                    solidAmount = crucibleRegistry.getMeltable(currentItem).getAmount();
                } else {
                    if (currentItem != null) {
                        currentItem = null;

                        markDirtyClient();
                    }

                    return;
                }
            }

            if (!itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).isItemEqual(currentItem.getItemStack())) {
                // For meltables with a really small "amount"
                while (heatRate > solidAmount && !itemHandler.getStackInSlot(0).isEmpty()) {
                    solidAmount += crucibleRegistry.getMeltable(currentItem).getAmount();
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

            if (heatRate > 0 && currentItem != null && crucibleRegistry.canBeMelted(currentItem)) {
                FluidStack toFill = new FluidStack(FluidRegistry.getFluid(crucibleRegistry.getMeltable(currentItem).getFluid()), heatRate);
                int filled = tank.fillInternal(toFill, true);
                solidAmount -= filled;

                if (filled > 0) {
                    markDirtyClient();
                }
            }
        }
    }

    @Override
    public int getHeatRate() {
        return ModConfig.crucible.woodenCrucibleSpeed;
    }
}
