package wraith.fabricaeexnihilo.api;

import wraith.fabricaeexnihilo.api.compatibility.FabricaeExNihiloModule;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;

public class FabricaeExNihiloAPI {

    public static BlockGenerator getBlockGenerator() {
        return wraith.fabricaeexnihilo.util.BlockGenerator.INSTANCE;
    }

    public static void registerCompatabilityModule(FabricaeExNihiloModule module) {
        MetaModule.INSTANCE.modules.add(module);
    }

}
