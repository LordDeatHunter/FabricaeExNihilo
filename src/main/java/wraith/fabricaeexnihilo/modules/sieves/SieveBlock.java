package wraith.fabricaeexnihilo.modules.sieves;

import net.devtech.arrp.json.recipe.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.util.ItemUtils;
import wraith.fabricaeexnihilo.util.VoxelShapeHelper;

public class SieveBlock extends Block implements BlockEntityProvider, Waterloggable {

    private final Identifier texture;
    private final Identifier craftIngredient1;
    private final Identifier craftIngredient2;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public SieveBlock(Identifier texture, Identifier craftIngredient1, Identifier craftIngredient2, Settings settings) {
        super(settings);
        this.texture = texture;
        this.craftIngredient1 = craftIngredient1;
        this.craftIngredient2 = craftIngredient2;
        setDefaultState(getStateManager().getDefaultState().with(WATERLOGGED, false));
    }
    public SieveBlock(Identifier texture, Identifier craftIngredient1, Identifier craftIngredient2) {
        this(texture, craftIngredient1, craftIngredient2, FabricBlockSettings.of(Material.WOOD));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        /*
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        */
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient() || pos == null) {
            return ActionResult.SUCCESS;
        }
        if (world.getBlockEntity(pos) instanceof SieveBlockEntity sieve) {
            return sieve.activate(state, player, hand, hit);
        }
        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SieveBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.isCreative()) {
            if (world.getBlockEntity(pos) instanceof SieveBlockEntity sieve) {
                var stack = ItemUtils.asStack(this);
                world.spawnEntity(ItemUtils.asEntity(stack, world, pos));
                if (!sieve.getMesh().isEmpty()) {
                    world.spawnEntity(ItemUtils.asEntity(sieve.getMesh(), world, pos));
                }
                if (!sieve.getContents().isEmpty()) {
                    world.spawnEntity(ItemUtils.asEntity(sieve.getContents(), world, pos));
                }
            }
        }
        super.onBreak(world, pos, state, player);
    }

    public JRecipe generateRecipe() {
        return JRecipe.shaped(
                JPattern.pattern(
                        "x x",
                        "xyx",
                        "z z"
                ),
                JKeys.keys()
                        .key("x",
                                JIngredient.ingredient()
                                        .item(craftIngredient1.toString())
                        )
                        .key("y",
                                JIngredient.ingredient()
                                        .item(craftIngredient2.toString())
                        )
                        .key("z",
                                JIngredient.ingredient()
                                        .item("minecraft:stick")
                        ),
                JResult.item(asItem())
        );
    }

    private static final VoxelShape SHAPE;

    static {
        SHAPE = VoxelShapeHelper.union(
                createCuboidShape(0.0, 0.0, 0.0, 2.0, 12.0, 2.0),
                createCuboidShape(14.0, 0.0, 0.0, 16.0, 12.0, 2.0),
                createCuboidShape(0.0, 0.0, 14.0, 2.0, 12.0, 16.0),
                createCuboidShape(14.0, 0.0, 14.0, 16.0, 12.0, 16.0),
                createCuboidShape(0.0, 8.0, 0.0, 16.0, 12.0, 16.0)
        );
    }

    public Identifier getTexture() {
        return texture;
    }

}
