package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

public class ModernIndustrializationApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("antimony");
        registries.registerOrePiece("iridium");
        registries.registerOrePiece("tin");
        registries.registerOrePiece("silver");
        registries.registerOrePiece("lead");
        registries.registerOrePiece("nickel");
        registries.registerOrePiece("platinum");
        registries.registerOrePiece("titanium");
        registries.registerOrePiece("tungsten");
        registries.registerOrePiece("uranium");
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("modern_industrialization");
    }

}
