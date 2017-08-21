package exnihilocreatio.barrel.modes.compost;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.tiles.TileBarrel;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BarrelItemHandlerCompost extends ItemStackHandler {

    @Setter
    TileBarrel barrel;

    public BarrelItemHandlerCompost(TileBarrel barrel) {
        super(1);
        this.barrel = barrel;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(stack)) {
            BarrelModeCompost mode = (BarrelModeCompost) this.barrel.getMode();

            if (mode != null && mode.getFillAmount() < 1) {
                ItemStack toReturn = stack.copy();
                toReturn.shrink(1);

                if (!simulate) {
                    mode.addItem(stack, barrel);
                }

                return toReturn;
            }
        }

        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }
}
