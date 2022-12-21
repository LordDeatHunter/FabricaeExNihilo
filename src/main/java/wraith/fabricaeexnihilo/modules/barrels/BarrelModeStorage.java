package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

@SuppressWarnings("UnstableApiUsage")
public interface BarrelModeStorage {
    default long insertItem(ItemVariant item, long maxAmount, TransactionContext transaction, BarrelItemStorage storage) {
        return 0;
    }

    default long extractItem(ItemVariant item, long maxAmount, TransactionContext transaction, BarrelItemStorage storage) {
        return 0;
    }

    default long insertFluid(FluidVariant fluid, long maxAmount, TransactionContext transaction, BarrelFluidStorage storage) {
        return 0;
    }

    default long extractFluid(FluidVariant fluid, long maxAmount, TransactionContext transaction, BarrelFluidStorage storage) {
        return 0;
    }

    default ItemVariant getItem() {
        return ItemVariant.blank();
    }

    default FluidVariant getFluid() {
        return FluidVariant.blank();
    }

    default long getItemAmount() {
        return 0;
    }

    default long getFluidAmount() {
        return 0;
    }

    default long getItemCapacity() {
        return 0;
    }

    default long getFluidCapacity() {
        return 0;
    }
}
