package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.barrels.modes.*;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.util.CodecUtils;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

@SuppressWarnings("UnstableApiUsage")
public class BarrelBlockEntity extends BaseBlockEntity implements EnchantableBlockEntity {
    
    public static final Identifier BLOCK_ENTITY_ID = id("barrel");
    public static final BlockEntityType<BarrelBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            BarrelBlockEntity::new,
            ModBlocks.BARRELS.values().toArray(new BarrelBlock[0])
    ).build(null);
    
    static {
        ItemStorage.SIDED.registerForBlockEntity((barrel, direction) -> barrel.itemStorage, TYPE);
        FluidStorage.SIDED.registerForBlockEntity((barrel, direction) -> barrel.fluidStorage, TYPE);
    }
    
    private int tickCounter;
    private BarrelMode mode;
    private final boolean isStone;
    public final Storage<ItemVariant> itemStorage;
    public final Storage<FluidVariant> fluidStorage;
    private final EnchantmentContainer enchantments = new EnchantmentContainer();
    
    public BarrelBlockEntity(BlockPos pos, BlockState state, boolean isStone) {
        super(TYPE, pos, state);
        this.mode = new EmptyMode();
        this.isStone = isStone;
        itemStorage = new BarrelItemStorage(this);
        fluidStorage = new BarrelFluidStorage(this);
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.modules.barrels.tickRate
                : world.random.nextInt(FabricaeExNihilo.CONFIG.modules.barrels.tickRate);
    }
    
    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, false);
    }
    
    public BarrelMode getMode() {
        return mode;
    }
    
    public void setMode(BarrelMode mode) {
        this.mode = mode;
        markDirty();
    }
    
    public EnchantmentContainer getEnchantmentContainer() {
        return enchantments;
    }
    
    public static void ticker(World world, BlockPos blockPos, BlockState blockState, BarrelBlockEntity barrelEntity) {
        barrelEntity.tick();
    }
    
    public void tick() {
        if (tickCounter <= 0) {
            tickCounter = FabricaeExNihilo.CONFIG.modules.barrels.tickRate;
            markDirty();
            mode.tick(this);
        } else {
            --tickCounter;
            markDirty();
        }
    }
    
    public int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }
    
    public int countBelow(Block block, int radius) {
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
        nbt.put("mode", CodecUtils.toNbt(BarrelMode.CODEC, mode));
        nbt.put("enchantments", enchantments.writeNbt());
    }
    
    private void readNbtWithoutWorldInfo(NbtCompound nbt) {
        mode = CodecUtils.fromNbt(BarrelMode.CODEC, nbt.getCompound("mode"));
        if (nbt.contains("enchantments")) {
            var readEnchantments = new EnchantmentContainer();
            readEnchantments.readNbt(nbt.getCompound("enchantments"));
            enchantments.setAllEnchantments(readEnchantments);
        }
    }
    
    public void spawnByproduct(ItemStack stack) {
        if (stack.isEmpty() || world == null) {
            return;
        }
        ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
    
    public void spawnEntity(EntityStack entityStack) {
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
    public BlockPos getLeakPos() {
        if (world == null) {
            return null;
        }
        var rand = world.random;
        var r = FabricaeExNihilo.CONFIG.modules.barrels.leakRadius;
        var leakPos = pos.add(rand.nextInt(2 * r + 1) - r, -rand.nextInt(2), rand.nextInt(2 * r + 1) - r);
        return World.isValid(leakPos) ? leakPos : null;
    }
    
    public ActionResult activate(@Nullable PlayerEntity player, @Nullable Hand hand) {
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
            
            // TODO: Bucket shouldn't lose contents in creative (hard to fix)
            long moved;
            if (amount == null) {
                moved = StorageUtil.move(fluidStorage, bucketFluidStorage, fluidVariant -> true, FluidConstants.BUCKET, t);
            } else {
                moved = StorageUtil.move(bucketFluidStorage, fluidStorage, fluidVariant -> true, FluidConstants.BUCKET, t);
            }
            if (moved != 0) {
                t.commit();
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}