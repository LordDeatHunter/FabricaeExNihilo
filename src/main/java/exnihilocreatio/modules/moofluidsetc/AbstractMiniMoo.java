package exnihilocreatio.modules.moofluidsetc;

import com.ricardothecoder.minimoos.common.configurations.Configs;
import com.ricardothecoder.minimoos.library.entities.EntityFluidMoo;
import net.minecraftforge.fluids.Fluid;

/**
 * This class wraps the cows from the MiniMoos mod with a standard interface.
 *
 * https://github.com/RicardoTheCoder/MiniMoos
 */
public class AbstractMiniMoo implements IAbstractCow {
    private final EntityFluidMoo cow;
    private final int maxDelay;

    public AbstractMiniMoo(EntityFluidMoo entity){
        cow = entity;
        maxDelay = (int) Math.ceil(Configs.FluidMoos.maxUseDelay / cow.getEfficiency());
    }

    @Override
    public int getAvailableFluid() {
        // Fraction of a full bucket.
        return Fluid.BUCKET_VOLUME * (maxDelay - cow.getDelay()) / maxDelay;

    }

    @Override
    public int addCooldownEquivalent(int millibuckets) {
        // Fraction of max cooldown added.
        int timeAdded = maxDelay * millibuckets / Fluid.BUCKET_VOLUME;
        // Set Cooldown
        cow.setDelay(cow.getDelay() + timeAdded);
        return timeAdded;
    }

    @Override
    public Fluid getFluid() {
        return cow.getFluid();
    }
}
