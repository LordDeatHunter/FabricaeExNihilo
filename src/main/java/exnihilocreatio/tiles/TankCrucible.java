package exnihilocreatio.tiles;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class TankCrucible extends FluidTank{

    private TileCrucibleBase tileCrucible;

    public TankCrucible(int capacity, TileCrucibleBase tileCrucible) {
        super(capacity);
        this.tileCrucible = tileCrucible;
    }

    public TankCrucible(@Nullable FluidStack fluidStack, int capacity, TileCrucibleBase tileCrucible) {
        super(fluidStack, capacity);
        this.tileCrucible = tileCrucible;
    }

    public TankCrucible(Fluid fluid, int amount, int capacity, TileCrucibleBase tileCrucible) {
        super(fluid, amount, capacity);
        this.tileCrucible = tileCrucible;
    }

    @Nullable
    @Override
    public FluidStack drainInternal(int maxDrain, boolean doDrain) {
        FluidStack stack = super.drainInternal(maxDrain, doDrain);
        tileCrucible.markDirtyClient();
        return stack;
    }
}
