package wraith.fabricaeexnihilo.modules.crucibles;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import net.devtech.arrp.json.recipe.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
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
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.util.ItemUtils;
import wraith.fabricaeexnihilo.util.VoxelShapeHelper;

public class CrucibleBlock extends BlockWithEntity implements BlockEntityProvider, InventoryProvider, AttributeProvider {

    private final Identifier texture;
    private final Identifier craftIngredient;

    public CrucibleBlock(Identifier texture, Identifier craftIngredient, FabricBlockSettings settings) {
        super(settings);
        this.texture = texture;
        this.craftIngredient = craftIngredient;
    }

    public CrucibleBlock(Identifier texture, Identifier craftIngredient) {
        this(texture, craftIngredient, FabricBlockSettings.of(Material.WOOD));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world == null || world.isClient() || pos == null) {
            return ActionResult.PASS;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            return crucible.activate(state, player, hand, hit);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        if (world == null) {
            return null;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            return crucible.getInventory();
        }
        return null;
    }

    @Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> attributeList) {
        if (world == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            attributeList.offer(crucible.getFluidExtractor());
            attributeList.offer(crucible.getItemInserter());
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (fromPos == null || fromPos != (pos == null ? null : pos.down()) || world == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            crucible.updateHeat();
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (world == null || world.isClient() || pos == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            crucible.updateHeat();
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * BlockEntity functions
     */

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleBlockEntity(pos, state, this.material == Material.STONE);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            EnchantmentHelper.get(itemStack).forEach(crucible.getEnchantments()::setEnchantmentLevel);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (player == null) {
            return;
        }
        if (!player.isCreative()) {
            if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
                var stack = ItemUtils.asStack(this);
                EnchantmentContainer.addEnchantments(stack, crucible.getEnchantments());
                var itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                world.spawnEntity(itemEntity);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    public JRecipe generateRecipe() {
        return JRecipe.shaped(
                JPattern.pattern(
                        "x x",
                        "x x",
                        "xxx"
                ),
                JKeys.keys()
                        .key("x",
                                JIngredient.ingredient()
                                        .item(craftIngredient.toString())
                        ),
                JResult.item(asItem())
        );
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, CrucibleBlockEntity.TYPE, CrucibleBlockEntity::ticker);
    }

    private static final VoxelShape SHAPE;

    static {
        SHAPE = VoxelShapeHelper.union(
                createCuboidShape(0.0, 0.0, 0.0, 3.0, 3.0, 3.0),
                createCuboidShape(0.0, 0.0, 13.0, 3.0, 3.0, 16.0),
                createCuboidShape(13.0, 0.0, 0.0, 16.0, 3.0, 3.0),
                createCuboidShape(13.0, 0.0, 13.0, 16.0, 3.0, 16.0),
                createCuboidShape(0.0, 3.0, 0.0, 16.0, 16.0, 16.0)
        );
    }

    public Material getMaterial() {
        return this.material;
    }

}