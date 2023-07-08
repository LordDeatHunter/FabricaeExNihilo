package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.function.Supplier;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public abstract class AbstractFluid extends FlowableFluid {

    private final boolean still;
    private final Supplier<FluidBlock> fluidBlockSupplier;
    private final Supplier<Item> bucketItemSupplier;
    private final Supplier<FlowableFluid> flowingSupplier;
    private final Supplier<FlowableFluid> stillSupplier;

    private final FluidSettings fluidSettings;

    public AbstractFluid(boolean still, FluidSettings fluidSettings, Supplier<FluidBlock> fluidBlockSupplier, Supplier<Item> bucketItemSupplier, Supplier<FlowableFluid> flowingSupplier, Supplier<FlowableFluid> stillSupplier) {
        this.still = still;
        this.fluidSettings = fluidSettings;
        this.fluidBlockSupplier = fluidBlockSupplier;
        this.bucketItemSupplier = bucketItemSupplier;
        this.flowingSupplier = flowingSupplier;
        this.stillSupplier = stillSupplier;
    }

    public FluidSettings getFluidSettings() {
        return fluidSettings;
    }

    @Override
    public BlockState toBlockState(FluidState fluidState) {
        return fluidBlockSupplier.get().getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(fluidState));
    }

    @Override
    public int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    @Override
    public float getBlastResistance() {
        return 100F;
    }

    @Override
    public int getFlowSpeed(WorldView world) {
        return 4;
    }

    @Override
    public Fluid getStill() {
        return stillSupplier.get();
    }

    @Override
    public Fluid getFlowing() {
        return flowingSupplier.get();
    }

    @Override
    public Item getBucketItem() {
        return bucketItemSupplier.get();
    }

    @Override
    public int getTickRate(WorldView world) {
        return 10;
    }

    @Override
    public boolean isInfinite(World world) {
        return fluidSettings.isInfinite();
    }

    @Override
    public boolean isStill(FluidState fluid) {
        return still;
    }

    @Override
    public int getLevel(FluidState fluidState) {
        return still ? 8 : fluidState != null ? fluidState.get(LEVEL) : 8;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
    }

    @Override
    protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
        super.appendProperties(builder);
        if (!still && builder != null) {
            builder.add(LEVEL);
        }
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !matchesType(fluid);
    }

    public void registerFluids() {
        Registry.register(Registries.FLUID, id(this.fluidSettings.getBasePath()), stillSupplier.get());
        Registry.register(Registries.FLUID, id("flowing_" + this.fluidSettings.getBasePath()), flowingSupplier.get());
    }

    public void registerFluidBlock() {
        Registry.register(Registries.BLOCK, id(fluidSettings.getBasePath()), fluidBlockSupplier.get());
    }

    public void registerBucket() {
        Registry.register(Registries.ITEM, id(fluidSettings.getBasePath() + "_bucket"), bucketItemSupplier.get());
    }

}
