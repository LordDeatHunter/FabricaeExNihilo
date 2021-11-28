package wraith.fabricaeexnihilo.modules.barrels;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import net.devtech.arrp.json.recipe.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.modules.ModEffects;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.util.ItemUtils;

public class BarrelBlock extends BlockWithEntity implements BlockEntityProvider, AttributeProvider, InventoryProvider {

    private static final VoxelShape SHAPE = createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    private Identifier texture;
    private Identifier craftIngredient1;
    private Identifier craftIngredient2;
    public FabricBlockSettings settings;

    public BarrelBlock(Identifier texture, Identifier craftIngredient1, Identifier craftIngredient2, FabricBlockSettings settings) {
        super(settings);
        this.texture = texture;
        this.craftIngredient1 = craftIngredient1;
        this.craftIngredient2 = craftIngredient2;
        this.settings = settings;
    }

    public BarrelBlock(Identifier texture, Identifier craftIngredient1, Identifier craftIngredient2) {
        this(texture, craftIngredient1, craftIngredient2, FabricBlockSettings.of(Material.WOOD));
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
            return ActionResult.CONSUME;
        }
        var blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof BarrelBlockEntity barrelBlock
                ? barrelBlock.activate(state, player, hand, hitResult)
                : ActionResult.CONSUME;
    }

    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> attributes) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BarrelBlockEntity barrelEntity) {
            attributes.offer(barrelEntity.getItemTransferable());
            attributes.offer(barrelEntity.getFluidTransferable());
        }
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return world != null && world.getBlockEntity(pos) instanceof BarrelBlockEntity barrelEntity ? barrelEntity.getInventory() : null;
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
                    var volume = FluidKeys.get(BloodFluid.STILL).withAmount(FluidAmount.of1620((int) (FluidAmount.BUCKET.as1620() * thorns / livingEntity.getMaxHealth())));
                    barrelEntity.getFluidTransferable().attemptInsertion(volume, Simulation.ACTION);
                }
            }
            if (!(livingEntity instanceof PlayerEntity) && !livingEntity.hasStatusEffect(ModEffects.MILKED)) {
                var milkingResult = FabricaeExNihiloRegistries.BARREL_MILKING.getResult(entity);
                if (milkingResult != null) {
                    var volume = milkingResult.getLeft();
                    var cooldown = milkingResult.getRight();
                    var duration = 72000;
                    if (blockEntity instanceof BarrelBlockEntity barrelEntity) {
                        barrelEntity.getFluidTransferable().attemptInsertion(volume, Simulation.ACTION);
                        duration = cooldown;
                    }
                    livingEntity.addStatusEffect(new StatusEffectInstance(ModEffects.MILKED, duration, 1, false, false, false));
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

    public JRecipe generateRecipe() {
        return JRecipe.shaped(
                JPattern.pattern(
                        "x x",
                        "x x",
                        "xyx"
                ),
                JKeys.keys()
                        .key("x",
                                JIngredient.ingredient()
                                        .item(craftIngredient1.toString())
                        )
                        .key("y",
                                JIngredient.ingredient()
                                        .item(craftIngredient2.toString())
                        ),
                JResult.item(asItem())
        );
    }

    public Identifier getTexture() {
        return texture;
    }

}