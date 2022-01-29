package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.util.Color;

public class ModernIndustrializationApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("antimony", new Color("#C5C5D6"));
        registries.registerOrePiece("iridium", Color.IRIDIUM);
        registries.registerOrePiece("tin", Color.TIN);
        registries.registerOrePiece("silver", Color.SILVER);
        registries.registerOrePiece("lead", Color.LEAD);
        registries.registerOrePiece("nickel", Color.NICKEL);
        registries.registerOrePiece("platinum", Color.PLATINUM);
        registries.registerOrePiece("titanium", Color.TITANIUM);
        registries.registerOrePiece("tungsten", Color.TUNGSTEN);
        registries.registerOrePiece("uranium", Color.URANIUM);
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("modern_industrialization");
    }

}
