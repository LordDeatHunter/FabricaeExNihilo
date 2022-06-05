package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

public class TechRebornApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("tin", registries.defaultItemSettings());
        registries.registerOrePiece("silver", registries.defaultItemSettings());
        registries.registerOrePiece("lead", registries.defaultItemSettings());
        registries.registerOrePiece("iridium", registries.defaultItemSettings());
        registries.registerOrePiece("tungsten", registries.defaultItemSettings());

        registries.registerMesh("carbon", Color.BLACK, 14, registries.defaultItemSettings());
        registries.registerWood("rubber", false, registries.woodenBlockSettings());
        registries.registerInfestedLeaves("rubber", new Identifier("techreborn:rubber_leaves"), registries.infestedLeavesBlockSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("techreborn");
    }

}
