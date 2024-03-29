package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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

    public final BarrelFluidStorage fluidStorage;
    public final BarrelItemStorage itemStorage;
    private final EnchantmentContainer enchantments = new EnchantmentContainer();
    private BarrelMode mode;
    private int tickCounter;

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        this.mode = new EmptyMode();
        itemStorage = new BarrelItemStorage(this);
        fluidStorage = new BarrelFluidStorage(this);
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.modules.barrels.tickRate
                : world.random.nextInt(FabricaeExNihilo.CONFIG.modules.barrels.tickRate);
    }

    public static void ticker(World world, BlockPos blockPos, BlockState blockState, BarrelBlockEntity barrelEntity) {
        barrelEntity.tick();
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

    public int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }

    public EnchantmentContainer getEnchantmentContainer() {
        return enchantments;
    }

    /**
     * Returns a valid BlockPos or null
     */
    public BlockPos getLeakPos() {
        if (world == null) {
            return null;
        }
        var rand = world.random;
        var r = FabricaeExNihilo.CONFIG.modules.barrels.leaking.radius;
        var leakPos = pos.add(rand.nextInt(2 * r + 1) - r, -rand.nextInt(2), rand.nextInt(2 * r + 1) - r);
        return World.isValid(leakPos) ? leakPos : null;
    }

    public BarrelMode getMode() {
        return mode;
    }

    public void setMode(BarrelMode mode) {
        this.mode = mode;
        markDirty();
    }

    public ActionResult insertFromHand(PlayerEntity player, Hand hand) {
        if (world == null)
            return ActionResult.PASS;

        var held = player.getStackInHand(hand);
        if (held.isEmpty()) return ActionResult.PASS;

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
        return ActionResult.PASS;
    }

    public boolean isFireproof() {
        return getCachedState().getBlock() instanceof BarrelBlock barrel && barrel.isFireproof();
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

    private void readNbtWithoutWorldInfo(NbtCompound nbt) {
        mode = CodecUtils.fromNbt(BarrelMode.CODEC, nbt.getCompound("mode"));
        enchantments.readNbt(nbt.getCompound("enchantments"));
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
        //TODO: play some particles
        entityStack.setSize(entityStack.getSize() - 1);
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

    public void precipitationTick(Biome.Precipitation precipitation) {
        mode.precipitationTick(this, precipitation);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeNbtWithoutWorldInfo(nbt);
    }

    private void writeNbtWithoutWorldInfo(NbtCompound nbt) {
        nbt.put("mode", CodecUtils.toNbt(BarrelMode.CODEC, mode));
        nbt.put("enchantments", enchantments.writeNbt());
    }
}