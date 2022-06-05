package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

public class ModernIndustrializationApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("antimony", registries.defaultItemSettings());
        registries.registerOrePiece("iridium", registries.defaultItemSettings());
        registries.registerOrePiece("tin", registries.defaultItemSettings());
        registries.registerOrePiece("silver", registries.defaultItemSettings());
        registries.registerOrePiece("lead", registries.defaultItemSettings());
        registries.registerOrePiece("nickel", registries.defaultItemSettings());
        registries.registerOrePiece("platinum", registries.defaultItemSettings());
        registries.registerOrePiece("titanium", registries.defaultItemSettings());
        registries.registerOrePiece("tungsten", registries.defaultItemSettings());
        registries.registerOrePiece("uranium", registries.defaultItemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("modern_industrialization");
    }

}
