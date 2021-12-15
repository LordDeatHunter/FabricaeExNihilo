package wraith.fabricaeexnihilo.modules.crucibles;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.item.ItemInsertable;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.util.ItemUtils;

import static alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter.ANYTHING;
import static wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries.*;

public class CrucibleBlockEntity extends BaseBlockEntity {

    private final boolean isStone;
    // What stack should be rendered for the queued fluid
    private ItemStack render = ItemStack.EMPTY;
    // Fluid to be made
    private FluidVolume queued = FluidKeys.EMPTY.withAmount(FluidAmount.ZERO);
    // Fluid already made
    private FluidVolume contents = FluidKeys.EMPTY.withAmount(FluidAmount.ZERO);
    private int heat = 0;
    private int tickCounter;

    public static final BlockEntityType<CrucibleBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            CrucibleBlockEntity::new,
            ModBlocks.CRUCIBLES.values().toArray(new CrucibleBlock[0])
    ).build(null);
    public static final Identifier BLOCK_ENTITY_ID = FabricaeExNihilo.id("crucible");


    public CrucibleBlockEntity(BlockPos pos, BlockState state, boolean isStone) {
        super(TYPE, pos, state);
        this.isStone = isStone;
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.modules.crucibles.tickRate
                : world.random.nextInt(FabricaeExNihilo.CONFIG.modules.crucibles.tickRate);
        itemInserter = new ItemInserter(this);
        fluidExtractor = new FluidExtractor(this);
        inventory = new CrucibleInventory(this);
    }

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, state.getMaterial() == Material.STONE);
    }

    /**
     * Inventories
     */
    private final ItemInserter itemInserter;
    private final FluidExtractor fluidExtractor;
    private final CrucibleInventory inventory;

    public boolean isStone() {
        return isStone;
    }

    public ItemStack getRender() {
        return render;
    }

    public FluidVolume getQueued() {
        return queued;
    }

    public FluidVolume getContents() {
        return contents;
    }

    public int getHeat() {
        return heat;
    }

    public int getTickCounter() {
        return tickCounter;
    }

    public EnchantmentContainer getEnchantments() {
        return enchantments;
    }

    public ItemInserter getItemInserter() {
        return itemInserter;
    }

    public FluidExtractor getFluidExtractor() {
        return fluidExtractor;
    }

    public CrucibleInventory getInventory() {
        return inventory;
    }

    /**
     * Enchantments
     */
    private final EnchantmentContainer enchantments = new EnchantmentContainer();

    public static void ticker(World world, BlockPos blockPos, BlockState blockState, CrucibleBlockEntity crucibleEntity) {
        crucibleEntity.tick();
    }

    public void tick() {
        if (queued.isEmpty() || contents.amount().isGreaterThanOrEqual(getMaxCapacity()) || (heat <= 0 && isStone)) {
            return;
        }
        if (--tickCounter <= 0) {
            if (!queued.isEmpty()) {
                var amt = Math.min(queued.amount().as1620(), getProcessingSpeed());
                var fluid = FluidAmount.of1620(amt);
                contents = queued.copy().fluidKey.withAmount(contents.copy().amount().add(fluid));
                queued.split(fluid);
                if (queued.amount().isLessThanOrEqual(FluidAmount.ZERO)) {
                    queued = FluidKeys.EMPTY.withAmount(FluidAmount.ZERO);
                }
                markDirty();
            }
            tickCounter = FabricaeExNihilo.CONFIG.modules.crucibles.tickRate;
        }
    }

    private int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }

    private int getFireAspectAdder() {
        return enchantments.getEnchantmentLevel(Enchantments.FIRE_ASPECT);
    }

    public int getProcessingSpeed() {
        return getEfficiencyMultiplier() * (isStone ? heat * FabricaeExNihilo.CONFIG.modules.crucibles.baseProcessRate : FabricaeExNihilo.CONFIG.modules.crucibles.woodenProcessingRate);
    }

    public FluidAmount getMaxCapacity() {
        return FluidAmount.BUCKET.mul((isStone ? FabricaeExNihilo.CONFIG.modules.crucibles.stoneVolume : FabricaeExNihilo.CONFIG.modules.crucibles.woodVolume));
    }

    public ActionResult activate(@Nullable BlockState state, @Nullable PlayerEntity player, @Nullable Hand hand, @Nullable BlockHitResult hitResult) {
        if (world == null || player == null) {
            return ActionResult.PASS;
        }
        var held = player.getStackInHand(hand == null ? player.getActiveHand() : hand);
        if (held == null) {
            held = ItemStack.EMPTY;
        }

        if (held.isEmpty()) {
            return ActionResult.PASS;
        }

        // Removing a bucket's worth of fluid
        if (held.getItem() instanceof IBucketItem bucket) {
            if (bucket.libblockattributes__getFluid(held) == FluidKeys.EMPTY) {
                var amount = bucket.libblockattributes__getFluidVolumeAmount();
                if (contents.amount().isGreaterThanOrEqual(amount)) {
                    var drained = fluidExtractor.attemptExtraction(ANYTHING, amount, Simulation.SIMULATE);
                    if (drained.amount().equals(amount)) {
                        var returnStack = bucket.libblockattributes__withFluid(contents.fluidKey);
                        if (!returnStack.isEmpty()) {
                            fluidExtractor.attemptExtraction(ANYTHING, amount, Simulation.ACTION);
                            if (!player.isCreative()) {
                                held.decrement(1);
                            }
                            player.giveItemStack(returnStack);
                            markDirty();
                            return ActionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        // Adding a meltable item
        var result = itemInserter.attemptInsertion(held, Simulation.ACTION);
        if (result.getCount() != held.getCount()) {
            if (!player.isCreative()) {
                held.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public void updateHeat() {
        if (world == null) {
            return;
        }
        var oldheat = heat;
        var state = world.getBlockState(pos.down());
        var block = state.getBlock();
        if (block instanceof FluidBlock) {
            var fluidState = state.getFluidState();
            heat = Math.round(CRUCIBLE_HEAT.getHeat(fluidState.getFluid()) * fluidState.getFluid().getHeight(fluidState, world, pos.down()));
        } else {
            heat = CRUCIBLE_HEAT.getHeat(block);
        }
        heat += getFireAspectAdder();
        if (heat != oldheat) {
            markDirty();
        }
    }

    /**
     * NBT Serialization section
     */

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeNbtWithoutWorldInfo(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt == null) {
            FabricaeExNihilo.LOGGER.warn("A crucible at " + pos + " is missing data.");
            return;
        }
        readNbtWithoutWorldInfo(nbt);
    }

    private void writeNbtWithoutWorldInfo(NbtCompound nbt) {
        nbt.put("render", render.writeNbt(new NbtCompound()));
        nbt.put("contents", contents.toTag());
        nbt.put("queued", queued.toTag());
        nbt.putInt("heat", heat);
        nbt.put("enchantments", enchantments.writeNbt());
    }

    private void readNbtWithoutWorldInfo(NbtCompound nbt) {
        render = ItemStack.fromNbt(nbt.getCompound("render"));
        contents = FluidVolume.fromTag(nbt.getCompound("contents"));
        queued = FluidVolume.fromTag(nbt.getCompound("queued"));
        heat = nbt.getInt("heat");
        if (nbt.contains("enchantments")) {
            var readEnchantments = new EnchantmentContainer();
            readEnchantments.readNbt(nbt.getCompound("enchantments"));
            enchantments.setAllEnchantments(readEnchantments);
        }
    }

    /**
     * FluidHandling
     */
    static record FluidExtractor(CrucibleBlockEntity crucible) implements FluidExtractable {

        @Override
        public FluidVolume attemptExtraction(FluidFilter filter, FluidAmount maxAmount, Simulation simulation) {
            if (!crucible.contents.isEmpty() && maxAmount.isGreaterThan(FluidAmount.ZERO) && filter != null && filter.matches(crucible.contents.fluidKey)) {
                var toDrain = maxAmount.min(crucible.contents.amount());
                if (simulation.isAction()) {
                    var toReturn = crucible.contents.copy().split(toDrain);
                    crucible.markDirty();
                    return toReturn;
                } else {
                    return crucible.contents.copy().fluidKey.withAmount(toDrain);
                }
            } else {
                return FluidKeys.EMPTY.withAmount(FluidAmount.ZERO);
            }
        }

    }

    /**
     * Item Handling
     */
    static record ItemInserter(CrucibleBlockEntity crucible) implements ItemInsertable {

        @Override
        public ItemStack attemptInsertion(ItemStack stack, Simulation simulation) {
            var result = (crucible.isStone ? CRUCIBLE_STONE : CRUCIBLE_WOOD).getResult(stack.getItem());
            if (result == null) {
                return stack;
            }
            if ((crucible.contents.canMerge(result) || crucible.contents.isEmpty())
                    && (crucible.queued.canMerge(result) || crucible.queued.isEmpty())
                    && crucible.contents.copy().amount().add(crucible.queued.amount()).add(result.amount()).isLessThanOrEqual(crucible.getMaxCapacity())) {
                if (simulation.isAction()) {
                    crucible.queued = result.fluidKey.withAmount(crucible.queued.amount().add(result.amount()));
                    if (stack.getItem() instanceof BlockItem) {
                        crucible.render = stack.copy();
                    } else if (crucible.isStone) {
                        crucible.render = ItemUtils.asStack(Blocks.COBBLESTONE);
                    } else {
                        crucible.render = ItemUtils.asStack(Blocks.OAK_LEAVES);
                    }
                    crucible.markDirty();
                }
                return ItemUtils.ofSize(stack, stack.getCount() - 1);
            }
            return stack;
        }
    }

    static record CrucibleInventory(CrucibleBlockEntity crucible) implements SidedInventory {

        @Override
        public ItemStack getStack(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public void markDirty() {
            crucible.markDirty();
        }

        @Override
        public void clear() {

        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            if (!stack.isEmpty()) {
                crucible.itemInserter.attemptInsertion(stack, Simulation.ACTION);
            }
        }

        @Override
        public ItemStack removeStack(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return false;
        }

        @Override
        public int[] getAvailableSlots(Direction side) {
            return new int[]{0};
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return false;
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean isEmpty() {
            return crucible.contents.copy().amount().add(crucible.queued.amount()).isLessThan(crucible.getMaxCapacity());
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            if (stack == null) {
                return false;
            }
            return (crucible.itemInserter.attemptInsertion(stack, Simulation.SIMULATE).getCount() != stack.getCount());
        }
    }

}