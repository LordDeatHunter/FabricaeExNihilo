package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

public class IndustrialRevolutionApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("tin");
        registries.registerOrePiece("silver");
        registries.registerOrePiece("lead");
        registries.registerOrePiece("tungsten");
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("indrev");
    }

}
