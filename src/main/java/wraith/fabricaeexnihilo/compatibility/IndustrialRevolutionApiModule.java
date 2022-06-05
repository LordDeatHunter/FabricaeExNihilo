package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

public class IndustrialRevolutionApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("tin", registries.defaultItemSettings());
        registries.registerOrePiece("silver", registries.defaultItemSettings());
        registries.registerOrePiece("lead", registries.defaultItemSettings());
        registries.registerOrePiece("tungsten", registries.defaultItemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("indrev");
    }

}
