package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateManager;

import java.util.ArrayList;
import java.util.List;

public class BaseFluidBlock extends FluidBlock {

    private final List<FluidState> statesByLevel = new ArrayList<>();

    public BaseFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
        statesByLevel.add(fluid.getStill(false));
        for(int i = 1; i < 8; ++i) {
            statesByLevel.add(fluid.getFlowing(8 - i, false));
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

}
