package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;

@SuppressWarnings("UnstableApiUsage")
public class BarrelItemStorage extends SnapshotParticipant<BarrelMode> implements SingleSlotStorage<ItemVariant> {
    public final BarrelBlockEntity barrel;
    
    public BarrelItemStorage(BarrelBlockEntity barrel) {
        this.barrel = barrel;
    }
    
    @Override
    protected void onFinalCommit() {
        barrel.markDirty();
    }
    
    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        return barrel.getMode().insertItem(resource, maxAmount, transaction, this);
    }
    
    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        return barrel.getMode().extractItem(resource, maxAmount, transaction, this);
    }
    
    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }
    
    @Override
    public ItemVariant getResource() {
        return barrel.getMode().getItem();
    }
    
    @Override
    public long getAmount() {
        return barrel.getMode().getItemAmount();
    }
    
    @Override
    public long getCapacity() {
        return barrel.getMode().getItemCapacity();
    }
    
    @Override
    protected BarrelMode createSnapshot() {
        return barrel.getMode().copy();
    }
    
    @Override
    protected void readSnapshot(BarrelMode snapshot) {
        barrel.setMode(snapshot);
    }
}
