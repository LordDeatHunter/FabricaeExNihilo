package wraith.fabricaeexnihilo.modules.barrels;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidTransferable;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.item.ItemTransferable;
import alexiil.mc.lib.attributes.item.filter.ExactItemStackFilter;
import alexiil.mc.lib.attributes.item.filter.ItemFilter;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.crafting.EntityStack;
import wraith.fabricaeexnihilo.api.crafting.Lootable;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.barrels.modes.*;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.util.FluidUtils;
import wraith.fabricaeexnihilo.util.ItemUtils;

public class BarrelBlockEntity extends BaseBlockEntity {

    public static final Identifier BLOCK_ENTITY_ID = FabricaeExNihilo.id("barrel");
    public static final BlockEntityType<BarrelBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            BarrelBlockEntity::new,
            ModBlocks.BARRELS.values().toArray(new BarrelBlock[0])
    ).build(null);

    private int tickCounter;
    // Inventories
    private final FluidTransferer fluidTransferable;
    private final ItemTransferer itemTransferable;
    private final BarrelInventory inventory;
    private BarrelMode mode;

    private final boolean isStone;

    public BarrelMode getMode() {
        return mode;
    }

    public BarrelInventory getInventory() {
        return inventory;
    }

    public EnchantmentContainer getEnchantmentContainer() {
        return enchantments;
    }

    public FluidTransferer getFluidTransferable() {
        return fluidTransferable;
    }

    public ItemTransferer getItemTransferable() {
        return itemTransferable;
    }
    // Enchantments

    EnchantmentContainer enchantments = new EnchantmentContainer();

    public BarrelBlockEntity(BlockPos pos, BlockState state, BarrelMode mode, boolean isStone) {
        super(TYPE, pos, state);
        fluidTransferable = new FluidTransferer(this);
        itemTransferable = new ItemTransferer(this);
        inventory = new BarrelInventory(this);
        this.mode = mode;
        this.isStone = isStone;
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.modules.barrels.tickRate
                : world.random.nextInt(FabricaeExNihilo.CONFIG.modules.barrels.tickRate);
    }

    public BarrelBlockEntity(BlockPos pos, BlockState state, BarrelMode mode) {
        this(pos, state, mode, false);
    }

    public BarrelBlockEntity(BlockPos pos, BlockState state, boolean isStone) {
        this(pos, state, new EmptyMode(), isStone);
    }

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, new EmptyMode(), false);
    }

    public static void ticker(World world, BlockPos blockPos, BlockState blockState, BarrelBlockEntity barrelEntity) {
        barrelEntity.tick();
    }

    public void tick() {
        if (tickCounter <= 0) {
            tickCounter = FabricaeExNihilo.CONFIG.modules.barrels.tickRate;
            markDirty();
            if (transformTick()) return;
            if (leakTick()) return;
            if (alchemyTick()) return;
            if (compostTick()) return;
        } else {
            --tickCounter;
            markDirty();
        }
    }

    private int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }

    private boolean transformTick() {
        if (world == null || world.isClient) {
            return false;
        }
        if (mode instanceof FluidMode fluidMode) {
            if (fluidMode.getFluid().amount().isGreaterThanOrEqual(FluidAmount.BUCKET)) {
                // Check block above for fluid on top
                var fluidState = world.getFluidState(pos.up());
                if (!fluidState.isEmpty()) {
                    var fluid = fluidState.getFluid();
                    var result = FabricaeExNihiloRegistries.BARREL_ON_TOP.getResult(fluidMode.getFluid(), fluid);
                    if (result != null) {
                        this.mode = result;
                        markDirty();
                        return true;
                    }
                }
                // Check block below for fluid transform
                var blockState = world.getBlockState(pos.down());
                if (!blockState.isAir()) {
                    var block = blockState.getBlock();
                    var result = FabricaeExNihiloRegistries.BARREL_TRANSFORM.getResult(fluidMode.getFluid(), block);
                    if (result != null) {
                        var num = countBelow(block, FabricaeExNihilo.CONFIG.modules.barrels.transformBoostRadius);
                        this.mode = new AlchemyMode(fluidMode, result, FabricaeExNihilo.CONFIG.modules.barrels.transformRate - (num - 1) * FabricaeExNihilo.CONFIG.modules.barrels.transformBoost);
                        markDirty();
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean compostTick() {
        if (!(mode instanceof CompostMode compostMode)) {
            return false;
        }
        if (compostMode.getProgress() >= 1.0) {
            mode = new ItemMode(compostMode.getResult().copy());
            return true;
        }
        if (compostMode.getAmount() >= 1.0) {
            compostMode.setProgress(compostMode.getProgress() + FabricaeExNihilo.CONFIG.modules.barrels.compostRate * getEfficiencyMultiplier());
            markDirty();
        }
        return true;
    }

    private boolean alchemyTick() {
        if (!(mode instanceof AlchemyMode alchemyMode)) {
            return false;
        }
        alchemyMode.setCountdown(alchemyMode.getCountdown() - getEfficiencyMultiplier());
        if (world != null && !world.isClient && alchemyMode.getCountdown() <= 0) {
            spawnEntity(alchemyMode.getToSpawn());
            mode = alchemyMode.getAfter();
            markDirty();
        }
        return true;
    }

    private boolean leakTick() {
        if (isStone || world == null || world.isClient) {
            return false;
        }
        if (mode instanceof FluidMode fluidMode) {
            var leakPos = getLeakPos();
            if (leakPos == null) {
                return false;
            }
            var leakResult = FabricaeExNihiloRegistries.BARREL_LEAKING.getResult(world.getBlockState(leakPos).getBlock(), fluidMode.getFluid());
            if (leakResult == null) {
                return false;
            }
            var leakAmount = leakResult.getRight();
            world.setBlockState(leakPos, leakResult.getLeft().getDefaultState());
            if (fluidMode.getFluid().amount().isGreaterThan(leakAmount)) {
                fluidMode.getFluid().split(leakAmount);
            } else {
                mode = new EmptyMode();
            }
            markDirty();
            return true;
        }
        return false;
    }

    private int countBelow(Block block, int radius) {
        var count = 0;
        if (world == null) {
            return count;
        }
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (world.getBlockState(pos.add(x, -1, z)).getBlock() == block) {
                    ++count;
                }
            }
        }
        return count;
    }

    // NBT Serialization section

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeNbtWithoutWorldInfo(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt == null) {
            FabricaeExNihilo.LOGGER.warn("A barrel at $pos is missing data.");
            return;
        }
        readNbtWithoutWorldInfo(nbt);
    }

    private void writeNbtWithoutWorldInfo(NbtCompound nbt) {
        nbt.put(mode.nbtKey(), mode.writeNbt());
        nbt.put("enchantments", enchantments.writeNbt());
    }

    private void readNbtWithoutWorldInfo(NbtCompound nbt) {
        mode = BarrelMode.of(nbt);
        if (nbt.contains("enchantments")) {
            var readEnchantments = new EnchantmentContainer();
            readEnchantments.readNbt(nbt.getCompound("enchantments"));
            enchantments.setAllEnchantments(readEnchantments);
        }
    }

    private void spawnByproduct(Lootable loot) {
        boolean useMax = world == null || world.random == null;
        var count = loot.getChances().stream().filter(chance -> chance > (useMax ? Double.MAX_VALUE : world.random.nextDouble())).toList().size();
        var stack = loot.getStack().copy();
        stack.setCount(count);
        spawnByproduct(stack);
    }

    private void spawnByproduct(ItemStack stack) {
        if (stack.isEmpty() || world == null) {
            return;
        }
        ItemScatterer.spawn(world, pos.up(), DefaultedList.copyOf(ItemStack.EMPTY, stack));
    }

    private void spawnEntity(EntityStack entityStack) {
        if (entityStack.isEmpty() || world == null || world.isClient) {
            return;
        }
        var entity = entityStack.getEntity((ServerWorld) world, pos.up((int) Math.ceil(entityStack.getType().getHeight())));
        if (entity == null) {
            return;
        }
        world.spawnEntity(entity);
        // TODO play some particles
        entityStack.setSize(entityStack.getSize() - 1);
    }

    /**
     * Returns a valid BlockPos or null
     */
    private BlockPos getLeakPos() {
        if (world == null) {
            return null;
        }
        var rand = world.random;
        var r = FabricaeExNihilo.CONFIG.modules.barrels.leakRadius;
        var leakPos = pos.add(rand.nextInt(2 * r + 1) - r, -rand.nextInt(2), rand.nextInt(2 * r + 1) - r);
        return World.isValid(leakPos) ? leakPos : null;
    }

    public ActionResult activate(@Nullable BlockState state, @Nullable PlayerEntity player, @Nullable Hand hand, @Nullable BlockHitResult hitResult) {
        if (world == null || player == null || hand == null) {
            return ActionResult.PASS;
        }
        if (mode instanceof ItemMode) {
            dropInventoryAtPlayer(player);
            return ActionResult.SUCCESS;
        }
        if (mode instanceof EmptyMode || mode instanceof CompostMode || mode instanceof FluidMode) {
            return insertFromHand(player, hand);
        } else {
            return ActionResult.PASS;
        }
    }

    public void dropInventoryAtPlayer(PlayerEntity player) {
        if (world == null || !(mode instanceof ItemMode itemMode)) {
            return;
        }
        var entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0625, pos.getZ() + 0.5, itemMode.getStack());
        entity.setVelocity(player.getPos().subtract(entity.getPos()).normalize().multiply(0.5));
        if (world != null) {
            world.spawnEntity(entity);
        }
        mode = new EmptyMode();
        markDirty();
    }

    public ActionResult insertFromHand(PlayerEntity player, Hand hand) {
        var held = player.getStackInHand(hand);
        if (held == null) {
            held = ItemStack.EMPTY;
        }
        var remaining = itemTransferable.attemptInsertion(held, Simulation.ACTION);
        if (remaining.getCount() != held.getCount()) {
            if (!player.isCreative()) {
                held.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        // Check for fluids
        if (!(held.getItem() instanceof IBucketItem bucket)) {
            return ActionResult.PASS;
        }
        // Filling with a fluid
        var volume = FluidUtils.proxyFluidVolume(bucket, held);
        var amount = bucket.libblockattributes__getFluidVolumeAmount();
        if (!volume.isEmpty()) {
            var leftover = fluidTransferable.attemptInsertion(volume, Simulation.ACTION);
            if (leftover.isEmpty()) {
                var returnStack = bucket.libblockattributes__drainedOfFluid(held);
                if (!player.isCreative()) {
                    held.decrement(1);
                }
                player.giveItemStack(returnStack);
                markDirty();
                return ActionResult.SUCCESS;
            }
        }
        // Removing a bucket's worth of fluid
        else if (mode instanceof FluidMode fluidMode) {
            var drained = fluidTransferable.attemptExtraction(fluidKey -> true, amount, Simulation.SIMULATE);
            if (drained.amount().equals(bucket.libblockattributes__getFluidVolumeAmount())) {
                var returnStack = bucket.libblockattributes__withFluid(fluidMode.getFluid().fluidKey);
                if (!returnStack.isEmpty()) {
                    fluidTransferable.attemptExtraction(fluidKey -> true, amount, Simulation.ACTION);
                    if (!player.isCreative()) {
                        held.decrement(1);
                    }
                    player.giveItemStack(returnStack);
                    markDirty();
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }

    // Fluid Inventory Management
    record FluidTransferer(BarrelBlockEntity barrel) implements FluidTransferable {

        @Override
        public FluidVolume attemptInsertion(FluidVolume volume, Simulation simulation) {
            if (barrel.mode instanceof EmptyMode) {
                var amount = volume.amount().min(FluidAmount.BUCKET);
                if (amount.as1620() > 0) {
                    var toTake = volume.withAmount(amount);
                    if (simulation.isAction()) {
                        barrel.mode = new FluidMode(toTake);
                        barrel.markDirty();
                    }
                    return FluidUtils.copyLess(volume, toTake.amount());
                }
            }
            if (barrel.mode instanceof FluidMode fluidMode) {
                if (fluidMode.getFluid().canMerge(volume)) {
                    var amount = volume.amount().min(FluidAmount.BUCKET.sub(fluidMode.getFluid().amount()));
                    if (amount.as1620() > 0) {
                        var toTake = fluidMode.getFluid().getFluidKey().withAmount(amount);
                        if (simulation.isAction()) {
                            fluidMode.getFluid().merge(toTake, Simulation.ACTION);
                            barrel.markDirty();
                        }
                        return FluidUtils.copyLess(volume, toTake.amount());
                    }
                }
            }

            return volume;
        }

        @Override
        public FluidVolume attemptExtraction(FluidFilter filter, FluidAmount amount, Simulation simulation) {
            if (!(barrel.mode instanceof FluidMode fluidMode) || !filter.matches(fluidMode.getFluid().getFluidKey())) {
                return FluidKeys.EMPTY.withAmount(FluidAmount.of1620(0));
            }
            var returnVolume = fluidMode.getFluid().withAmount(fluidMode.getFluid().amount().min(amount));
            if (!simulation.isAction()) {
                return returnVolume;
            }
            if (amount.isGreaterThanOrEqual(fluidMode.getFluid().amount())) {
                barrel.mode = new EmptyMode();
            } else {
                fluidMode.getFluid().split(amount);
            }
            barrel.markDirty();
            return returnVolume;
        }

    }

    // Item Inventory Management
    static record ItemTransferer(BarrelBlockEntity barrel) implements ItemTransferable {

        @Override
        public ItemStack attemptInsertion(ItemStack stack, Simulation simulation) {
            if (barrel.mode instanceof EmptyMode) {
                var recipe = FabricaeExNihiloRegistries.BARREL_COMPOST.getRecipe(stack);
                if (recipe == null) {
                    return stack;
                }
                if (simulation.isAction()) {
                    barrel.mode = new CompostMode(recipe.result(), recipe.amount(), recipe.color());
                    barrel.markDirty();
                }
                var returnStack = stack.copy();
                returnStack.decrement(1);
                return returnStack;
            }
            if (barrel.mode instanceof FluidMode fluidMode) {
                var result = FabricaeExNihiloRegistries.BARREL_ALCHEMY.getRecipe(fluidMode.getFluid(), stack);
                if (result == null) {
                    return stack;
                }
                if (simulation.isAction()) {
                    if (result.getDelay() == 0) {
                        barrel.mode = result.getProduct();
                        barrel.spawnEntity(result.getToSpawn().copy());
                    } else {
                        barrel.mode = new AlchemyMode(fluidMode, result.getProduct(), result.getToSpawn().copy(), result.getDelay());
                    }
                    barrel.spawnByproduct(result.getByproduct());
                    barrel.markDirty();
                }
                barrel.markDirty();
                var returnStack = stack.copy();
                returnStack.decrement(1);
                return returnStack;
            }
            if (barrel.mode instanceof CompostMode compostMode) {
                var recipe = FabricaeExNihiloRegistries.BARREL_COMPOST.getRecipe(stack);
                if (recipe == null) {
                    return stack;
                }
                if (compostMode.getAmount() < 1.0 && recipe.result().isItemEqual(compostMode.getResult())) {
                    if (simulation.isAction()) {
                        compostMode.setAmount(Math.min(compostMode.getAmount() + recipe.amount(), 1.0));
                        compostMode.setColor(recipe.color());
                        compostMode.setProgress(0);
                        barrel.markDirty();
                    }
                    return ItemUtils.ofSize(stack, stack.getCount() - 1);
                }
            }
            return stack;
        }

        @Override
        public ItemStack attemptExtraction(ItemFilter filter, int amount, Simulation simulation) {
            if (!(barrel.mode instanceof ItemMode itemMode) || !filter.matches(itemMode.getStack())) {
                return ItemStack.EMPTY;
            }
            var returnStack = itemMode.getStack().copy();
            returnStack.setCount(Math.min(itemMode.getStack().getCount(), amount));
            if (!simulation.isAction()) {
                return returnStack;
            }
            if (amount >= itemMode.getStack().getCount()) {
                barrel.mode = new EmptyMode();
            } else {
                itemMode.getStack().decrement(amount);
            }
            barrel.markDirty();
            return returnStack;
        }

    }

    static record BarrelInventory(BarrelBlockEntity barrel) implements SidedInventory {


        @Override
        public ItemStack getStack(int slot) {
            return (barrel.mode instanceof ItemMode itemMode) ? itemMode.getStack() : ItemStack.EMPTY;
        }

        @Override
        public void markDirty() {
            barrel.markDirty();
        }

        @Override
        public void clear() {
            if (barrel.mode instanceof ItemMode) {
                barrel.mode = new EmptyMode();
            }
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            if (!stack.isEmpty()) {
                barrel.itemTransferable.attemptInsertion(stack, Simulation.ACTION);
            }
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            return barrel.itemTransferable.attemptExtraction(stack -> true, amount, Simulation.ACTION);
        }

        @Override
        public ItemStack removeStack(int slot) {
            if (!(barrel.mode instanceof ItemMode itemMode)) {
                return ItemStack.EMPTY;
            }
            var stack = itemMode.getStack();
            barrel.mode = new EmptyMode();
            barrel.markDirty();
            return stack;
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return false;
        }

        @Override
        public int[] getAvailableSlots(Direction side) {
            return new int[0];
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return !barrel.itemTransferable.attemptExtraction(new ExactItemStackFilter(stack), stack.getCount(), Simulation.SIMULATE).isEmpty();
        }

        @Override
        public boolean isEmpty() {
            return !(barrel.mode instanceof ItemMode);
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return stack != null && (barrel.itemTransferable.attemptInsertion(stack, Simulation.SIMULATE).getCount() != stack.getCount());
        }

    }

}
