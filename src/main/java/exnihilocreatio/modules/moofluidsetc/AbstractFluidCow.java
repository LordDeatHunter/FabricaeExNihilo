package exnihilocreatio.modules.moofluidsetc;

import ftblag.fluidcows.entity.EntityFluidCow;
import ftblag.fluidcows.gson.FCConfig;
import net.minecraftforge.fluids.Fluid;

/**
 * This class wraps the cows from the FluidCows mod with a standard interface.
 *
 * https://github.com/DoctorFTB/FluidCows
 */
public class AbstractFluidCow implements IAbstractCow {
    private EntityFluidCow cow;
    private int maxDelay;

    public AbstractFluidCow(EntityFluidCow entity){
        cow = entity;
        maxDelay = FCConfig.getWorldCD(cow.fluid.getName());
    }

    @Override
    public int getAvailableFluid() {
        // Fraction of a full bucket.
        return Fluid.BUCKET_VOLUME * (maxDelay - cow.getCD()) / maxDelay ;
    }

    @Override
    public int addCooldownEquivalent(int millibuckets) {
        // Fraction of max cooldown added.
        int timeAdded = maxDelay * millibuckets / Fluid.BUCKET_VOLUME;
        // Set Cooldown
        cow.updateCD(cow.getCD() + timeAdded);
        return timeAdded;
    }

    @Override
    public Fluid getFluid() {
        return cow.fluid;
    }
}
