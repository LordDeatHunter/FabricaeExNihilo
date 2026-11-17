package wraith.fabricaeexnihilo.modules.crucibles;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Iterator;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class CrucibleBlockEntity extends BaseBlockEntity implements EnchantableBlockEntity, SidedInventory {

    public static final Identifier BLOCK_ENTITY_ID = id("crucible");

    public static final BlockEntityType<CrucibleBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            CrucibleBlockEntity::new,
            ModBlocks.CRUCIBLES.values().toArray(new CrucibleBlock[0])
    ).build();

    static {
        ItemStorage.SIDED.registerForBlockEntity((crucible, direction) -> crucible.itemStorage, TYPE);
        FluidStorage.SIDED.registerForBlockEntity((crucible, direction) -> crucible.fluidStorage, TYPE);
    }

    private final EnchantmentContainer enchantments = new EnchantmentContainer();
    private final Storage<FluidVariant> fluidStorage = new CrucibleFluidStorage();
    private final Storage<ItemVariant> itemStorage = new CrucibleItemStorage();
    private long contained = 0;
    private FluidVariant fluid = FluidVariant.blank();
    private int heat = 0;
    private boolean requiresFireproof = false;
    private long queued = 0;
    private ItemStack renderStack = ItemStack.EMPTY;
    private int tickCounter;

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.get().crucibles().tickRate()
                : world.random.nextInt(FabricaeExNihilo.CONFIG.get().crucibles().tickRate());
    }

    public ActionResult activate(@Nullable PlayerEntity player, @Nullable Hand hand) {
        if (world == null || player == null) {
            return ActionResult.PASS;
        }
        var held = player.getStackInHand(hand == null ? player.getActiveHand() : hand);
        if (held == null || held.isEmpty()) {
            return ActionResult.PASS;
        }

        var bucketFluidStorage = FluidStorage.ITEM.find(held, ContainerItemContext.ofPlayerHand(player, hand));
        if (bucketFluidStorage != null) {
            var amount = StorageUtil.move(fluidStorage, bucketFluidStorage, fluid -> true, Long.MAX_VALUE, null);
            if (amount > 0) {
                markDirty();
                return ActionResult.SUCCESS;
            }
            // A fluid container aimed at a crucible always belongs to the crucible -
            // whether this crucible can take that fluid or not. Returning PASS here
            // handed the click to vanilla `BucketItem`, which poured the lava (or
            // water) onto whatever block happened to sit next to the crucible.
            // FAIL keeps the click consumed without pretending anything happened.
            return ActionResult.FAIL;
        }

        try (var t = Transaction.openOuter()) {
            var amount = itemStorage.insert(ItemVariant.of(held), 1, t);
            if (amount > 0) {
                t.commit();
                if (!player.isCreative()) {
                    held.decrement((int) amount);
                }
                markDirty();
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    public long getContained() {
        return contained;
    }

    private int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }

    @Override
    public EnchantmentContainer getEnchantmentContainer() {
        return enchantments;
    }

    public EnchantmentContainer getEnchantments() {
        return enchantments;
    }

    private int getFireAspectAdder() {
        return enchantments.getEnchantmentLevel(Enchantments.FIRE_ASPECT);
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public long getMaxCapacity() {
        return FluidConstants.BUCKET * (isFireproof() ? FabricaeExNihilo.CONFIG.get().crucibles().stoneVolume() : FabricaeExNihilo.CONFIG.get().crucibles().woodVolume());
    }

    public int getProcessingSpeed() {
        return getEfficiencyMultiplier() * (isFireproof() ? (requiresFireproof ? heat : 1) * FabricaeExNihilo.CONFIG.get().crucibles().stoneProcessingRate() : FabricaeExNihilo.CONFIG.get().crucibles().woodProcessingRate());
    }

    public long getQueued() {
        return queued;
    }

    /**
     * The solid currently responsible for the fluid inside.
     * <p>
     * `queued` drains to 0 the moment the last of the input has melted, so anything that
     * keyed the display on `queued` alone made the crucible look like it had been emptied
     * out - the item model vanished from the block and the tooltips dropped both the solid
     * and, with an empty-ish fluid amount, the fluid line too. The solid is still the thing
     * the fluid came from, so keep reporting it while either the queue or the tank is
     * non-empty, and drop it once the crucible is actually empty again.
     */
    public ItemStack getDisplayStack() {
        if (renderStack.isEmpty()) return ItemStack.EMPTY;
        if (queued <= 0 && contained <= 0) return ItemStack.EMPTY;
        return renderStack;
    }

    public ItemStack getRenderStack() {
        return renderStack;
    }

    public boolean isFireproof() {
        return getCachedState().getBlock() instanceof CrucibleBlock crucible && crucible.isFireproof();
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        renderStack = view.read("render", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        fluid = view.read("fluid", FluidVariant.CODEC).orElse(FluidVariant.blank());
        contained = view.getLong("contained", 0L);
        queued = view.getLong("queued", 0L);
        heat = view.getInt("heat", 0);
        requiresFireproof = view.getBoolean("requiresFireproof", true);
        enchantments.readData(view);
        // `readData` runs on BOTH sides. Recomputing the heat here meant every update
        // packet that reached a client clobbered the authoritative `heat` the server had
        // just sent: the client's chunk may not have the block below it yet (or may hold
        // a stale copy), so the lookup came back empty and `heat` silently reset to 0.
        // The server already calls `updateHeat()` itself whenever the heat can change
        // (neighbour update, being placed, enchantments), and the result is part of the
        // synced data - so consumers just trust what they were given.
        if (world != null && !world.isClient()) {
            updateHeat(); // Enchantments affect heat
        }
        // Older saves (or a crucible downgraded to a wooden one) could hold more than the
        // current capacity. Clamp on load instead of leaving the block permanently stuck.
        contained = Math.max(0, Math.min(contained, getMaxCapacity()));
        queued = Math.max(0, queued);
    }

    public void tick() {
        var capacity = getMaxCapacity();
        if (queued == 0 || contained >= capacity || (requiresFireproof && (heat <= 0 || !isFireproof()))) {
            return;
        }
        if (--tickCounter <= 0) {
            // Never process past the capacity: with Efficiency / a hot block below, a single
            // step can be thousands of mB, which used to push `contained` above the limit.
            // The leftover stays in `queued` and is processed once there is room again.
            var amount = Math.min(queued, Math.min(getProcessingSpeed(), capacity - contained));
            if (amount > 0) {
                contained += amount;
                queued -= amount;
            }
            markDirty();
            broadcast();
            tickCounter = FabricaeExNihilo.CONFIG.get().crucibles().tickRate();
        }
    }

    /**
     * Push the new state to every client that can see the block.
     * <p>
     * Without this the tooltips and the item model only refreshed when the block itself
     * changed (placing it, inserting an item, extracting fluid) - the amount crept up on
     * the server while the client kept showing whatever it read minutes ago, which is why
     * a mostly-full crucible could still claim "60mb" in the tooltip.
     */
    private void broadcast() {
        if (world instanceof ServerWorld serverWorld) {
            var state = getCachedState();
            serverWorld.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
    }

    public void updateHeat() {
        if (world == null) {
            return;
        }
        var oldHeat = heat;
        var state = world.getBlockState(pos.down());
        heat = CrucibleHeatRecipe.find(state, world).map(RecipeEntry::value).map(CrucibleHeatRecipe::getHeat).orElse(0);
        if (state.getBlock() instanceof FluidBlock) {
            heat = (int) (heat * state.getFluidState().getHeight());
        }
        heat += getFireAspectAdder();
        if (heat != oldHeat) {
            markDirty();
        }
    }

    /**
     * NBT Serialization section
     */

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.put("render", ItemStack.OPTIONAL_CODEC, renderStack);
        view.put("fluid", FluidVariant.CODEC, fluid);
        view.putLong("contained", contained);
        view.putLong("queued", queued);
        view.putInt("heat", heat);
        view.putBoolean("requiresFireproof", requiresFireproof);
        enchantments.writeData(view);
    }

    private record CrucibleSnapshot(long contained, long queued, FluidVariant fluid, boolean requiresFireproof,
                                    ItemStack renderStack) {
    }

    private class CrucibleFluidStorage extends SnapshotParticipant<CrucibleSnapshot> implements SingleSlotStorage<FluidVariant>, ExtractionOnlyStorage<FluidVariant> {

        @Override
        protected CrucibleSnapshot createSnapshot() {
            return new CrucibleSnapshot(contained, queued, fluid, requiresFireproof, renderStack);
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            if (!resource.equals(fluid))
                return 0;

            var amount = Math.min(maxAmount, contained);
            updateSnapshots(transaction);
            contained -= amount;
            return amount;
        }

        @Override
        public long getAmount() {
            return contained;
        }

        @Override
        public long getCapacity() {
            return getMaxCapacity();
        }

        @Override
        public FluidVariant getResource() {
            return fluid;
        }

        @Override
        public boolean isResourceBlank() {
            return fluid.isBlank();
        }

        @Override
        protected void readSnapshot(CrucibleSnapshot snapshot) {
            contained = snapshot.contained;
            queued = snapshot.queued;
            fluid = snapshot.fluid;
            requiresFireproof = snapshot.requiresFireproof;
            renderStack = snapshot.renderStack.copy();
        }
    }

    private class CrucibleItemStorage extends SnapshotParticipant<CrucibleSnapshot> implements InsertionOnlyStorage<ItemVariant>, SingleSlotStorage<ItemVariant> {
        @Override
        protected CrucibleSnapshot createSnapshot() {
            return new CrucibleSnapshot(contained, queued, fluid, requiresFireproof, renderStack.copy());
        }

        @Override
        public long getAmount() {
            return 0;
        }

        @Override
        public long getCapacity() {
            return 1;
        }

        @Override
        public ItemVariant getResource() {
            return ItemVariant.blank();
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            var recipeOptional = CrucibleRecipe.find(resource.toStack(), isFireproof(), world).map(RecipeEntry::value);
            if (recipeOptional.isEmpty()) return 0;
            var recipe = recipeOptional.get();
            if (!recipe.getFluid().equals(fluid) && !fluid.isBlank()) return 0;
            if (recipe.getAmount() > getMaxCapacity() - contained - queued) return 0;
            updateSnapshots(transaction);
            fluid = recipe.getFluid();
            queued += recipe.getAmount();
            requiresFireproof = recipe.requiresFireproofCrucible();
            renderStack = resource.toStack();
            return 1;
        }

        // Compiler dumb
        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return InsertionOnlyStorage.super.extract(resource, maxAmount, transaction);
        }

        @Override
        public boolean isResourceBlank() {
            return true;
        }

        @Override
        protected void readSnapshot(CrucibleSnapshot snapshot) {
            contained = snapshot.contained;
            queued = snapshot.queued;
            fluid = snapshot.fluid;
            requiresFireproof = snapshot.requiresFireproof;
            renderStack = snapshot.renderStack.copy();
        }

        // Parents have conflicting impls, pick one
        @Override
        @NotNull
        public java.util.Iterator<StorageView<ItemVariant>> iterator() {
            return SingleSlotStorage.super.iterator();
        }
    }

    // --- Slot based access ---------------------------------------------------
    // A crucible holds no item: everything inserted is immediately converted into
    // `queued` fluid. Vanilla hoppers (and any mod that goes through the vanilla
    // container API instead of the Fabric transfer API) never look at
    // `ItemStorage.SIDED`, so without this they cannot feed a crucible at all.
    //
    // We expose a single *virtual* input slot that always reports itself empty -
    // hoppers only push into a slot whose `getStack` is empty - and accept items
    // through `setStack` (which is what `HopperBlockEntity.transfer` calls).
    // `getAvailableSlots` deliberately hides the slot from the DOWN face so a
    // hopper underneath cannot try to pull fluid out as items.

    public static final int ITEM_SLOT = 0;
    private static final int[] NO_SLOTS = new int[0];
    private static final int[] ITEM_SLOTS = {ITEM_SLOT};

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        // Nothing is ever stored as an item.
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        // The slot is a pure input funnel; the item is consumed on the spot.
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot != ITEM_SLOT || stack.isEmpty()) return;
        // `setStack` returns void, so a hopper cannot learn how much was accepted.
        // Feed the whole stack in one transaction - the storage stops accepting as
        // soon as the crucible has no room left for another melt.
        try (var transaction = Transaction.openOuter()) {
            itemStorage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            transaction.commit();
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return canInsert(slot, stack, null);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if (slot != ITEM_SLOT || stack.isEmpty()) return false;
        return canAccept(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, @Nullable Direction side) {
        // Fluids leave through FluidStorage, never as items.
        return false;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return side == Direction.DOWN ? NO_SLOTS : ITEM_SLOTS;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return world != null
                && world.getBlockEntity(pos) == this
                && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clear() {
        // No item inventory to clear.
    }

    /**
     * Whether this crucible would accept {@code stack} right now. Mirrors the
     * checks in {@link CrucibleItemStorage#insert} without mutating anything, so
     * that automation can ask before offering items.
     */
    private boolean canAccept(ItemStack stack) {
        if (world == null || world.isClient()) return false;
        var recipeOptional = CrucibleRecipe.find(stack, isFireproof(), world).map(RecipeEntry::value);
        if (recipeOptional.isEmpty()) return false;
        var recipe = recipeOptional.get();
        if (!recipe.getFluid().equals(fluid) && !fluid.isBlank()) return false;
        return recipe.getAmount() <= getMaxCapacity() - contained - queued;
    }
}