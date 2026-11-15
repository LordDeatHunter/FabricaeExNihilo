package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipe;

public class BarrelItemStorage extends SnapshotParticipant<BarrelBlockEntity.Snapshot> implements SingleSlotStorage<ItemVariant> {
    private final BarrelBlockEntity barrel;

    public BarrelItemStorage(BarrelBlockEntity barrel) {
        this.barrel = barrel;
    }

    @Override
    public long insert(ItemVariant item, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(item, maxAmount);
        if (barrel.isCrafting()) return 0;
        if (barrel.getState() == BarrelState.ITEM) return 0;

        var recipe = BarrelRecipe.findInsert(barrel, item);
        if (recipe.isEmpty()) return 0;

        // Vanilla hoppers call `setStack`, which returns void: anything we do not
        // consume is silently dropped by the caller. So consume the whole offered
        // stack here. Insert-triggered barrel recipes are instant (duration 0), and
        // instant ones can simply be applied repeatedly until the barrel is full.
        updateSnapshots(transaction);

        if (recipe.get().value().getDuration() != 0) {
            // Timed recipe: it occupies the barrel for a while, accept a single item.
            barrel.beginRecipe(recipe.get());
            return 1;
        }

        var consumed = 0L;
        var limit = Math.min(maxAmount, item.toStack().getMaxCount());
        for (var i = 0L; i < limit; i++) {
            if (barrel.getState() == BarrelState.ITEM) break;
            if (barrel.getState() == BarrelState.COMPOST && barrel.getCompostLevel() >= 1) break;
            var next = BarrelRecipe.findInsert(barrel, item);
            if (next.isEmpty()) break;
            if (!barrel.consumeInstantRecipe(next.get())) break;
            consumed++;
        }
        return consumed;
    }

    @Override
    public long extract(ItemVariant item, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(item, maxAmount);
        if (barrel.isCrafting()) return 0;
        if (barrel.getState() != BarrelState.ITEM) return 0;
        if (!item.equals(getResource())) return 0;

        var extracted = Math.min(getAmount(), maxAmount);
        if (extracted == 0) return 0;

        updateSnapshots(transaction);
        // Copy first: `getItem()` hands out the live field, so decrementing it in
        // place would mutate the barrel before `setItem` runs and make the snapshot
        // rollback restore an already-shrunk stack.
        var stack = barrel.getItem().copy();
        stack.decrement((int) extracted);
        barrel.setItem(stack); // this handles resetting the state with empty stack

        return extracted;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public ItemVariant getResource() {
        return ItemVariant.of(barrel.getItem());
    }

    @Override
    public long getAmount() {
        return barrel.getItem().getCount();
    }

    @Override
    public long getCapacity() {
        return barrel.getItem().getMaxCount();
    }

    @Override
    protected BarrelBlockEntity.Snapshot createSnapshot() {
        return barrel.new Snapshot();
    }

    @Override
    protected void readSnapshot(BarrelBlockEntity.Snapshot snapshot) {
        snapshot.apply();
    }
}
