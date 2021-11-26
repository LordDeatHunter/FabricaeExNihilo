package wraith.fabricaeexnihilo.api;

import wraith.fabricaeexnihilo.api.compatibility.IFabricaeExNihiloModule;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.util.BlockGenerator;

public class FabricaeExNihiloAPI {

    public static IBlockGenerator getBlockGenerator() {
        return BlockGenerator.INSTANCE;
    }

    public static void registerCompatabilityModule(IFabricaeExNihiloModule module) {
        MetaModule.INSTANCE.modules.add(module);
    }

}
