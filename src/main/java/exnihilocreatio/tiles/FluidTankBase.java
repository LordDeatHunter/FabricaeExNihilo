package exnihilocreatio.tiles;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class FluidTankBase extends FluidTank {

    private final BaseTileEntity tileEntity;

    public FluidTankBase(int capacity, BaseTileEntity tileEntity) {
        super(capacity);
        this.tileEntity = tileEntity;
    }

    public FluidTankBase(@Nullable FluidStack fluidStack, int capacity, BaseTileEntity tileEntity) {
        super(fluidStack, capacity);
        this.tileEntity = tileEntity;
    }

    public FluidTankBase(Fluid fluid, int amount, int capacity, BaseTileEntity tileEntity) {
        super(fluid, amount, capacity);
        this.tileEntity = tileEntity;
    }

    @Override
    protected void onContentsChanged() {
        // updates the tile entity for the sake of things that detect when contents change (such as comparators)
        tileEntity.markDirtyClient();
    }
}
