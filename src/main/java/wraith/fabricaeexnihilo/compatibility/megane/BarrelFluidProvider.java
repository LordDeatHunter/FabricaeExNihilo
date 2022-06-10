package wraith.fabricaeexnihilo.compatibility.megane;

import lol.bai.megane.api.provider.FluidProvider;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;

class BarrelFluidProvider extends FluidProvider<BarrelBlockEntity> {
    @Override
    public int getSlotCount() {
        if (!(getObject().getMode() instanceof FluidMode)) return 0;
        return 1;
    }
    
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @Nullable Fluid getFluid(int slot) {
        if (!(getObject().getMode() instanceof FluidMode mode)) return null;
        return mode.getFluid().getFluid();
    }
    
    @Override
    public double getStored(int slot) {
        if (!(getObject().getMode() instanceof FluidMode mode)) return 0;
        return droplets(mode.getFluidAmount());
    }
    
    @Override
    public double getMax(int slot) {
        if (!(getObject().getMode() instanceof FluidMode mode)) return 0;
        return droplets(mode.getFluidCapacity());
    }
}
