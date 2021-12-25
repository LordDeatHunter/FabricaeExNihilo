package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;

@SuppressWarnings("UnstableApiUsage")
public class BarrelFluidStorage extends SnapshotParticipant<BarrelMode> implements SingleSlotStorage<FluidVariant> {
    public final BarrelBlockEntity barrel;
    
    public BarrelFluidStorage(BarrelBlockEntity barrel) {
        this.barrel = barrel;
    }
    
    @Override
    protected void onFinalCommit() {
        barrel.markDirty();
    }
    
    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return barrel.getMode().insertFluid(resource, maxAmount, transaction, this);
    }
    
    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return barrel.getMode().extractFluid(resource, maxAmount, transaction, this);
    }
    
    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }
    
    @Override
    public FluidVariant getResource() {
        return barrel.getMode().getFluid();
    }
    
    @Override
    public long getAmount() {
        return barrel.getMode().getFluidAmount();
    }
    
    @Override
    public long getCapacity() {
        return barrel.getMode().getFluidCapacity();
    }
    
    @Override
    protected BarrelMode createSnapshot() {
        return barrel.getMode().copy();
    }
    
    @Override
    protected void readSnapshot(BarrelMode snapshot) {
        barrel.setMode(snapshot);
    }
    
    private void setBarrelMode(BarrelMode mode) {
        barrel.setMode(mode);
    }
}
