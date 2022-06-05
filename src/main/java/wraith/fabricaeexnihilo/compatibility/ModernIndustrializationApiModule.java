package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

import static wraith.fabricaeexnihilo.modules.ModItems.itemSettings;

public class ModernIndustrializationApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("antimony", itemSettings());
        registries.registerOrePiece("iridium", itemSettings());
        registries.registerOrePiece("tin", itemSettings());
        registries.registerOrePiece("silver", itemSettings());
        registries.registerOrePiece("lead", itemSettings());
        registries.registerOrePiece("nickel", itemSettings());
        registries.registerOrePiece("platinum", itemSettings());
        registries.registerOrePiece("titanium", itemSettings());
        registries.registerOrePiece("tungsten", itemSettings());
        registries.registerOrePiece("uranium", itemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("modern_industrialization");
    }

}
