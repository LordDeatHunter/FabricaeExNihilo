package wraith.fabricaeexnihilo.compatibility.jade;

import net.minecraft.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.JadeIds;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlock;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlock;

public class FENJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new BarrelProvider(), BarrelBlock.class);
        registration.registerBlockComponent(new CrucibleProvider(), CrucibleBlock.class);
        registration.registerBlockComponent(new SieveProvider(), SieveBlock.class);
        registration.registerBlockComponent(new StrainerProvider(), StrainerBlock.class);
        registration.registerBlockComponent(new InfestingLeavesProvider(), InfestingLeavesBlock.class);

        // Jade's universal plugin renders any FluidStorage as a generic line, which
        // shows a bare "0 / 1B" for an empty barrel and duplicates the fluid line
        // BarrelProvider already draws. Remove that generic line for barrels only.
        // This runs after every provider has appended, so our own lines survive.
        registration.addTooltipCollectedCallback((box, accessor) -> {
            if (accessor.getTarget() instanceof Block block && block instanceof BarrelBlock) {
                box.getTooltip().remove(JadeIds.UNIVERSAL_FLUID_STORAGE);
            }
        });
    }
}
