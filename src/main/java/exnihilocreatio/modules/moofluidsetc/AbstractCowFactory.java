package exnihilocreatio.modules.moofluidsetc;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Loader;

/**
 * Create an IAbstractCow from any one of the MooFluid-Cow-like entities.
 */
public class AbstractCowFactory {
    public static IAbstractCow getCow(Entity entity){
        if(Loader.isModLoaded("moofluids") && entity instanceof com.robrit.moofluids.common.entity.EntityFluidCow){
            return new AbstractMooFluids((com.robrit.moofluids.common.entity.EntityFluidCow) entity);
        }
        else if(Loader.isModLoaded("fluidcows") && entity instanceof ftblag.fluidcows.entity.EntityFluidCow){
            return new AbstractFluidCow((ftblag.fluidcows.entity.EntityFluidCow) entity);
        }
        else if(Loader.isModLoaded("minimoos") && entity instanceof com.ricardothecoder.minimoos.library.entities.EntityFluidMoo){
            return new AbstractMiniMoo((com.ricardothecoder.minimoos.library.entities.EntityFluidMoo) entity);
        }
        return null;
    }
}
