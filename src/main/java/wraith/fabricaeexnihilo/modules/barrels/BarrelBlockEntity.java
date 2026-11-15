package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipe;
import wraith.fabricaeexnihilo.util.CodecUtils;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import wraith.fabricaeexnihilo.recipe.RecipeHelper;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class BarrelBlockEntity extends BaseBlockEntity implements EnchantableBlockEntity, SidedInventory, Hopper {
    public static final Identifier BLOCK_ENTITY_ID = id("barrel");

    public static final BlockEntityType<BarrelBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            BarrelBlockEntity::new,
            ModBlocks.BARRELS.values().toArray(new BarrelBlock[0])
    ).build();

    static {
        ItemStorage.SIDED.registerForBlockEntity((barrel, direction) -> barrel.itemStorage, TYPE);
        FluidStorage.SIDED.registerForBlockEntity((barrel, direction) -> barrel.fluidStorage, TYPE);
    }

    public final BarrelFluidStorage fluidStorage;
    public final BarrelItemStorage itemStorage;
    private final EnchantmentContainer enchantments = new EnchantmentContainer();
    private int tickCounter;
    private Identifier lazeRecipeId;

    private BarrelState state = BarrelState.EMPTY;
    private FluidVariant fluid = FluidVariant.blank();
    private long fluidAmount = 0;
    private ItemStack stack = ItemStack.EMPTY; // Should only be non-empty in the item or compost state
    private float compostLevel = 0;
    private RecipeEntry<BarrelRecipe> recipe = null;
    private float recipeProgress = 0;

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        fluidStorage = new BarrelFluidStorage(this);
        itemStorage = new BarrelItemStorage(this);
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.get().barrels().tickRate()
                : world.random.nextInt(FabricaeExNihilo.CONFIG.get().barrels().tickRate());
    }

    public ActionResult activate(@Nullable PlayerEntity player, @Nullable Hand hand) {
        if (world == null || player == null || hand == null || isCrafting()) {
            return ActionResult.PASS;
        }
        if (state == BarrelState.ITEM) {
            dropInventoryAtPlayer(player);
            return ActionResult.SUCCESS;
        }
        return insertFromHand(player, hand);
    }

    public void dropInventoryAtPlayer(PlayerEntity player) {
        if (world == null || state != BarrelState.ITEM) {
            return;
        }
        var entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0625, pos.getZ() + 0.5, stack.copy());
        entity.setVelocity(player.getEntityPos().subtract(entity.getEntityPos()).normalize().multiply(0.5));
        if (world != null) {
            world.spawnEntity(entity);
        }
        setItem(ItemStack.EMPTY);
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState state) {
        if (world != null && getState() == BarrelState.ITEM) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), getItem().copy());
        }
        super.onBlockReplaced(pos, state);
    }

    public int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }

    public EnchantmentContainer getEnchantmentContainer() {
        return enchantments;
    }

    public ActionResult insertFromHand(PlayerEntity player, Hand hand) {
        if (world == null)
            return ActionResult.PASS;

        var held = player.getStackInHand(hand);
        if (held.isEmpty()) return ActionResult.SUCCESS;

        try (Transaction t = Transaction.openOuter()) {
            var inserted = (int) itemStorage.insert(ItemVariant.of(held), held.getCount(), t);
            if (inserted != 0) {
                if (!player.isCreative()) {
                    held.decrement(inserted);
                }
                t.commit();
                return ActionResult.SUCCESS;
            }
        }

        var bucketFluidStorage = FluidStorage.ITEM.find(held, ContainerItemContext.ofPlayerHand(player, hand));
        if (bucketFluidStorage == null) return ActionResult.PASS;

        try (Transaction t = Transaction.openOuter()) {
            var amount = StorageUtil.findExtractableContent(bucketFluidStorage, t);

            long moved;
            if (amount == null) {
                // Barrel to bucket
                var fluid = fluidStorage.getResource();
                moved = StorageUtil.move(fluidStorage, bucketFluidStorage, fluidVariant -> true, FluidConstants.BUCKET, t);
                if (moved != 0)
                    world.playSound(null, pos, FluidVariantAttributes.getFillSound(fluid), SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                // Bucket to barrel
                if (player.isCreative()) {
                    moved = fluidStorage.insert(amount.resource(), amount.amount(), t);
                } else {
                    moved = StorageUtil.move(bucketFluidStorage, fluidStorage, fluidVariant -> true, FluidConstants.BUCKET, t);
                }

                if (moved != 0)
                    world.playSound(null, pos, FluidVariantAttributes.getEmptySound(fluidStorage.getResource()), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            if (moved != 0) {
                t.commit();
                return ActionResult.SUCCESS;
            }
        }
        // A fluid container aimed at a barrel is meant for the barrel. Returning PASS
        // would hand the click to vanilla `BucketItem`, which pours the fluid onto a
        // neighbouring block instead of leaving it in the bucket.
        return ActionResult.FAIL;
    }

    public boolean isFireproof() {
        return getCachedState().getBlock() instanceof BarrelBlock barrel && barrel.isFireproof();
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        enchantments.writeData(view);
        view.putString("state", state.getId());
        view.put("fluid", FluidVariant.CODEC, fluid);
        view.putLong("fluidAmount", fluidAmount);
        view.put("stack", ItemStack.OPTIONAL_CODEC, stack);
        view.putFloat("compostLevel", compostLevel);
        if (recipe != null)
            view.putString("recipe", recipe.id().toString());
        view.putFloat("recipeProgress", recipeProgress);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        enchantments.readData(view);
        state = BarrelState.byId(view.getString("state", ""));
        fluid = view.read("fluid", FluidVariant.CODEC).orElse(FluidVariant.blank());
        fluidAmount = view.getLong("fluidAmount", 0L);
        stack = view.read("stack", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        compostLevel = view.getFloat("compostLevel", 0f);
        lazeRecipeId = view.getOptionalString("recipe").map(Identifier::tryParse).orElse(null);
        recipeProgress = view.getFloat("recipeProgress", 0f);
    }

    public void tick() {
        if (isCrafting() && world != null && world.isClient()) {
            var random = world.random;
            world.addParticleClient(ParticleTypes.SMOKE,
                    pos.getX() + 0.5 + random.nextGaussian() * 0.2,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5 + random.nextGaussian() * 0.2,
                    0.0,
                    0.0,
                    0.0);
        }
        if (tickCounter <= 0) {
            tickCounter = FabricaeExNihilo.CONFIG.get().barrels().tickRate();
            markDirty();
            tickRecipe();
        } else {
            --tickCounter;
            markDirty();
        }
    }

    private void tickRecipe() {
        if (world == null/* || world.isClient*/) return;

        if (getRecipe() != null) {
            BarrelRecipe currentRecipe = getRecipe().value();
            if (!currentRecipe.canContinue(world, this)) {
                recipe = null;
                recipeProgress = 0;
                return;
            }

            var duration = currentRecipe.getDuration();
            recipeProgress += 1f / duration * getEfficiencyMultiplier();
            if (recipeProgress >= 1) {
                finishRecipe();
            }
            markDirty();
            markForUpdate();
            return;
        }

        switch (state) {
            case EMPTY, FLUID -> BarrelRecipe.findTick(this).ifPresent(recipe -> {
                this.recipe = recipe;
                if (recipe.value().getDuration() == 0) {
                    finishRecipe(); // instant recipe
                }
                markDirty();
                markForUpdate();
            });
            case ITEM -> {}
            case COMPOST -> {
                if (compostLevel < 1) break;
                recipeProgress += (float) (FabricaeExNihilo.CONFIG.get().barrels().compostRate() * getEfficiencyMultiplier());
                if (recipeProgress >= 1) finishCompost();
                markDirty();
                markForUpdate();
            }
        }

    }

    public void setFluid(FluidVariant fluid, long amount) {
        if (amount == 0 || fluid.isBlank()) {
            this.fluid = FluidVariant.blank();
            this.fluidAmount = 0;
            this.state = BarrelState.EMPTY;
        } else {
            this.fluid = fluid;
            this.fluidAmount = amount;
            this.state = BarrelState.FLUID;
        }
        markDirty();
        markForUpdate();
    }

    public void setItem(ItemStack stack) {
        setFluid(FluidVariant.blank(), 0);
        if (stack.isEmpty()) {
            this.state = BarrelState.EMPTY;
            this.stack = ItemStack.EMPTY;
        } else {
            this.state = BarrelState.ITEM;
            this.stack = stack.copy();
        }
        markDirty();
        markForUpdate();
    }

    public void fillCompost(ItemStack result, float increment) {
        switch (state) {
            case EMPTY:
                state = BarrelState.COMPOST;
                stack = result.copy();
            case COMPOST:
                if (!ItemStack.areEqual(stack, result)) throw new IllegalStateException("Tired to compost incompatible item");
                compostLevel += increment;
                break;
            default:
                throw new IllegalStateException("Can't compost in state: " + state);
        }
        markDirty();
        markForUpdate();
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public long getFluidAmount() {
        return fluidAmount;
    }

    public ItemStack getItem() {
        return stack;
    }

    public BarrelState getState() {
        return state;
    }

    public boolean isCrafting() {
        // `recipeProgress` is shared between timed recipes and the compost pile:
        // composting advances it too (COMPOST state, 0..1), and it is reset when
        // the finished item is pulled out. Keying this on `recipeProgress > 0`
        // therefore reports "busy" for a barrel that is merely holding a finished
        // compost output, which blocks the next automation insert (canInsert and
        // BarrelItemStorage both bail out on isCrafting) - the barrel then needs a
        // manual right click to start over. Only a *running recipe* counts.
        return recipe != null;
    }

    void beginRecipe(RecipeEntry<BarrelRecipe> recipe) {
        this.recipe = recipe;
    }

    /**
     * Run a zero-duration recipe to completion right now.
     * <p>
     * Automation paths (vanilla hoppers go through {@code setStack}, which returns
     * void) hand us a whole stack in one call. A barrel can only track a single
     * {@code recipe} at a time, so feeding a stack one item per tick would silently
     * drop the rest. Instant recipes - which is what every insert-triggered barrel
     * recipe is - can simply be applied repeatedly within the same call.
     *
     * @return true if a recipe was applied, false if the barrel could not take it
     */
    boolean consumeInstantRecipe(RecipeEntry<BarrelRecipe> recipe) {
        if (!(world instanceof ServerWorld serverWorld)) return false;
        if (recipe.value().getDuration() != 0) return false;
        recipe.value().apply(serverWorld, this);
        markDirty();
        markForUpdate();
        return true;
    }

    private void finishRecipe() {
        if (world instanceof ServerWorld serverWorld) {
            getRecipe().value().apply(serverWorld, this);
        }
        recipe = null;
        recipeProgress = 0;
        markForUpdate();
    }

    private void finishCompost() {
        // The compost action is what put `dirt` (or whatever result) into `stack`
        // as a preview while the pile was filling; turning the finished pile into a
        // real output is just switching the state, the stack is already there.
        state = BarrelState.ITEM;
        compostLevel = 0;
        recipeProgress = 0;
        markDirty();
        markForUpdate();
    }

    // --- Slot based access ---------------------------------------------------
    // A barrel is *not* a real container: it holds at most one "thing" that may be
    // a fluid, a compost pile or the finished output. Automation (vanilla hoppers
    // included) can only see it through the Inventory / SidedInventory API, so we
    // expose two virtual slots that share the same single-value storage:
    //
    //   slot 0 = INPUT  - accepts items a barrel recipe is triggered by
    //                     (e.g. saplings for composting). Always empty, because the
    //                     item is consumed into `compostLevel` / a running recipe.
    //   slot 1 = OUTPUT - the finished item, may only be pulled out.
    //
    // They must be *separate* slots: vanilla hoppers only insert into a slot that
    // reports `getStack(slot).isEmpty()`, and only extract from one that does not.
    // Sharing a single slot makes an input-feeding hopper consider the barrel full
    // (composting stores the result as a "ghost" stack) and refuse to push.

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private static final int SLOT_COUNT = 2;
    private static final int[] SLOT_IDS = {INPUT_SLOT, OUTPUT_SLOT};

    @Override
    public int size() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return getStack(INPUT_SLOT).isEmpty() && getStack(OUTPUT_SLOT).isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        // Returns the live field on purpose. Vanilla `HopperBlockEntity.transfer`
        // reads a target slot with `getStack` and then merges *into that object*
        // (`increment`), only falling back to `setStack` for an empty slot. Handing
        // out a copy therefore made every merge silently no-op: the hopper believed
        // it had moved the item, the barrel kept it, and the same item was "pulled"
        // again and again. `setStack` below is what makes the empty-slot path work.
        if (slot != OUTPUT_SLOT) return ItemStack.EMPTY;
        if (getState() != BarrelState.ITEM) return ItemStack.EMPTY;
        // Merging mutates `stack` directly, so the state can go stale: a hopper that
        // merges its whole payload out leaves an empty stack behind while `state`
        // still says ITEM, and then `canExtract` keeps advertising an item that is
        // no longer there. Re-sync on every read instead of trusting the field.
        if (stack.isEmpty()) {
            setItem(ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot != OUTPUT_SLOT || getState() != BarrelState.ITEM || stack.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }
        // `split` already clamps to our count and decrements in place, so `stack`
        // ends up holding the remainder. When the last item leaves it becomes empty
        // and the state must follow, otherwise `getStack` keeps reporting the (now
        // empty) output slot as filled and automation sees a phantom item.
        var removed = stack.split(amount);
        if (stack.isEmpty()) {
            setItem(ItemStack.EMPTY);
        } else {
            markDirty();
            markForUpdate();
        }
        return removed;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot != OUTPUT_SLOT || getState() != BarrelState.ITEM) return ItemStack.EMPTY;
        var removed = stack.copy();
        setItem(ItemStack.EMPTY);
        return removed;
    }

    @Override
    public void setStack(int slot, ItemStack newStack) {
        if (slot == OUTPUT_SLOT) {
            // Reachable from `HopperBlockEntity.transfer`: it writes the destination
            // slot back through `setStack` for an empty slot, and hoppers (including
            // the compressed-blocks ones) put the item back here when the receiving
            // inventory rejected it. Putting it back into the output slot is what the
            // caller expects; dropping it would delete the item.
            if (newStack.isEmpty()) {
                if (getState() == BarrelState.ITEM) setItem(ItemStack.EMPTY);
            } else if (getState() == BarrelState.ITEM || getState() == BarrelState.EMPTY) {
                setItem(newStack);
            }
            return;
        }
        if (slot != INPUT_SLOT || newStack.isEmpty()) return;
        // Hopper feeding: route through the recipe-aware storage so the inserted
        // item actually starts (or continues) a barrel recipe.
        // NB: this method returns void, so a vanilla hopper cannot learn how much
        // was accepted and assumes the whole stack went in. BarrelItemStorage.insert
        // therefore consumes the full offered stack for instant recipes; a timed
        // recipe can only take one, so the remainder is lost. That is unavoidable
        // through the vanilla container API - mods using ItemStorage.SIDED get the
        // accurate count instead.
        try (var transaction = Transaction.openOuter()) {
            itemStorage.insert(ItemVariant.of(newStack), newStack.getCount(), transaction);
            transaction.commit();
        }
    }
    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return world != null
                && world.getBlockEntity(pos) == this
                && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        // Both slots are visible from every face: the facing direction alone cannot
        // tell "pull the finished item out" from "feed an input in" (a hopper above
        // and a hopper below both look DOWN / UP on the barrel side).
        return SLOT_IDS;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        // Hopper checks this before canInsert. The default implementation compares
        // against getStack(), which is empty for the input slot by design.
        return canInsert(slot, stack, null);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        // Automation may feed items in whenever a barrel recipe accepts them:
        // EMPTY starts a new recipe, FLUID continues one (e.g. alchemy inputs).
        // An ITEM barrel already holds its output and must not take more.
        if (slot != INPUT_SLOT || stack.isEmpty()) return false;
        if (isCrafting()) return false;
        if (world == null || world.isClient()) return false;
        // A full compost pile must be processed before anything else goes in.
        if (getState() == BarrelState.COMPOST && compostLevel >= 1) return false;
        // The recipe itself decides whether the state allows this item at all.
        return BarrelRecipe.findInsert(this, ItemVariant.of(stack)).isPresent();
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, @Nullable Direction side) {
        // Only the output may be pulled out by automation.
        if (slot != OUTPUT_SLOT) return false;
        return getState() == BarrelState.ITEM && !this.stack.isEmpty();
    }

    @Override
    public void clear() {
        if (getState() == BarrelState.ITEM) {
            setItem(ItemStack.EMPTY);
        }
    }

    // --- Hopper -------------------------------------------------------------
    // Implementing net.minecraft.block.entity.Hopper (instead of only
    // SidedInventory) is what makes hoppers above the barrel push items in:
    // HopperBlockEntity.transfer() is only reached for a target Inventory, and
    // several stack-transfer helpers additionally require `instanceof Hopper`.
    // Without it the barrel only ever received items through the Fabric
    // ItemStorage API, which vanilla hoppers never consult.

    @Override
    public double getHopperX() {
        return pos.getX() + 0.5;
    }

    @Override
    public double getHopperY() {
        return pos.getY() + 0.5;
    }

    @Override
    public double getHopperZ() {
        return pos.getZ() + 0.5;
    }

    @Override
    public boolean canBlockFromAbove() {
        // Surface is flat and opaque, so entities inside the block above must not
        // be picked up - matches the vanilla hopper behaviour.
        return true;
    }

    @SuppressWarnings("unchecked")
    private RecipeEntry<BarrelRecipe> getRecipe() {
        if (lazeRecipeId != null && world != null) {
            var recipeEntry = RecipeHelper.get(lazeRecipeId, world);
            recipe = (RecipeEntry<BarrelRecipe>) recipeEntry.filter(r -> r.value() instanceof BarrelRecipe).orElse(null);
            lazeRecipeId = null;
        }
        return recipe;
    }

    public float getRecipeProgress() {
        return recipeProgress;
    }

    public float getCompostLevel() {
        return compostLevel;
    }

    public class Snapshot {
        private final BarrelState state;
        private final FluidVariant fluid;
        private final long fluidAmount;
        private final ItemStack stack;
        private final float compostLevel;
        private final RecipeEntry<BarrelRecipe> recipe;
        private final float recipeProgress;

        public Snapshot() {
            this.state = BarrelBlockEntity.this.state;
            this.fluid = BarrelBlockEntity.this.fluid;
            this.fluidAmount = BarrelBlockEntity.this.fluidAmount;
            this.stack = BarrelBlockEntity.this.stack.copy();
            this.compostLevel = BarrelBlockEntity.this.compostLevel;
            this.recipeProgress = BarrelBlockEntity.this.recipeProgress;
            this.recipe = BarrelBlockEntity.this.getRecipe();
        }

        public void apply() {
            BarrelBlockEntity.this.state = this.state;
            BarrelBlockEntity.this.fluid = this.fluid;
            BarrelBlockEntity.this.fluidAmount = this.fluidAmount;
            // Copy on restore. `Snapshot` outlives a single transaction and can be
            // replayed more than once, so handing its stack to the field would make
            // both share one mutable ItemStack: the next `split`/`decrement` on the
            // barrel would silently shrink the snapshot too, and a later rollback
            // would "restore" an already-consumed stack - which reads in game as the
            // finished output refusing to disappear no matter how often it is pulled.
            BarrelBlockEntity.this.stack = this.stack.copy();
            BarrelBlockEntity.this.compostLevel = this.compostLevel;
            BarrelBlockEntity.this.recipeProgress = this.recipeProgress;
            BarrelBlockEntity.this.recipe = this.recipe;
        }
    }
}