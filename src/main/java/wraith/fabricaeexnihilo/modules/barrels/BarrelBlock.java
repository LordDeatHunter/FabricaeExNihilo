package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModEffects;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

@SuppressWarnings("UnstableApiUsage")
public class BarrelBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE = createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    
    public FabricBlockSettings settings;
    
    public BarrelBlock(FabricBlockSettings settings) {
        super(settings);
        this.settings = settings;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world == null || world.isClient || pos == null) {
            return ActionResult.SUCCESS;
        }
        var blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof BarrelBlockEntity barrelBlock
                ? barrelBlock.activate(player, hand)
                : ActionResult.PASS;
    }
    
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (player != null && !player.isCreative() && world.getBlockEntity(pos) instanceof BarrelBlockEntity barrelEntity) {
            var x = pos.getX();
            var y = pos.getY();
            var z = pos.getZ();
            var stack = ItemUtils.asStack(this);
            EnchantmentContainer.addEnchantments(stack, barrelEntity.getEnchantmentContainer());
            world.spawnEntity(new ItemEntity(world, x, y, z, stack));
            if (barrelEntity.getMode() instanceof ItemMode itemMode) {
                world.spawnEntity(new ItemEntity(world, x, y, z, itemMode.getStack()));
            }
        }
        super.onBreak(world, pos, state, player);
    }
    
    // Milking
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null && entity instanceof LivingEntity livingEntity) {
            if (FabricaeExNihilo.CONFIG.modules.barrels.enableBleeding && blockEntity instanceof BarrelBlockEntity barrelEntity) {
                var thorns = barrelEntity.getEnchantmentContainer().getEnchantmentLevel(Enchantments.THORNS);
                if (thorns > 0 && livingEntity.damage(DamageSource.CACTUS, thorns / 2F)) {
                    var amount = FluidConstants.BUCKET * thorns / livingEntity.getMaxHealth();
                    var storage = FluidStorage.SIDED.find(world, pos, state, blockEntity, Direction.UP);
                    if (storage != null) {
                        try (Transaction t = Transaction.openOuter()) {
                            storage.insert(FluidVariant.of(BloodFluid.STILL), (long) amount, t);
                            t.commit();
                        }
                    }
                }
            }
            if (!(livingEntity instanceof PlayerEntity) && !livingEntity.hasStatusEffect(ModEffects.MILKED)) {
                var recipe = MilkingRecipe.find(livingEntity.getType(), world);
                if (recipe.isPresent()) {
                    if (blockEntity instanceof BarrelBlockEntity barrel) {
                        long inserted;
                        try (Transaction t = Transaction.openOuter()) {
                            inserted = barrel.fluidStorage.insert(FluidVariant.of(MilkFluid.STILL), recipe.get().getAmount(), t);
                            t.commit();
                        }
                        if (inserted > 0) {
                            livingEntity.addStatusEffect(new StatusEffectInstance(ModEffects.MILKED, recipe.get().getCooldown(), 1, false, false, false));
                        }
                    }
                }
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BarrelBlockEntity.TYPE, BarrelBlockEntity::ticker);
    }
    
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelBlockEntity(pos, state, this.material == Material.STONE);
    }
    
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) instanceof BarrelBlockEntity barrelEntity) {
            EnchantmentHelper.get(itemStack).forEach((enchantment, level) -> barrelEntity.enchantments.setEnchantmentLevel(enchantment, level));
        }
    }
    
}