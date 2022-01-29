package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.util.Color;

public class IndustrialRevolutionApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("tin", Color.TIN);
        registries.registerOrePiece("silver", Color.SILVER);
        registries.registerOrePiece("lead", Color.LEAD);
        registries.registerOrePiece("tungsten", Color.TUNGSTEN);
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("indrev");
    }

}
