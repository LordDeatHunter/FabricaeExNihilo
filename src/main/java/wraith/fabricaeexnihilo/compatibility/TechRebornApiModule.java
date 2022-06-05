package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

public class TechRebornApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("tin");
        registries.registerOrePiece("silver");
        registries.registerOrePiece("lead");
        registries.registerOrePiece("iridium");
        registries.registerOrePiece("tungsten");
        // No raw ores
        // registry.accept("aluminum", new OreDefinition(Color.ALUMINUM, OreDefinition.PieceShape.FINE, OreDefinition.BaseMaterial.SAND));
        // registry.accept("zinc", new OreDefinition(Color.ZINC, OreDefinition.PieceShape.FINE, OreDefinition.BaseMaterial.NETHERRACK));
        // registries.registerOrePiece("platinum", Color.PLATINUM);

        registries.registerMesh("carbon", Color.BLACK, 14);
        registries.registerWood("rubber");
        registries.registerInfestedLeaves("rubber", new Identifier("techreborn:rubber_leaves"));
        registries.registerSeed("rubber", new Identifier("techreborn:rubber_sapling"));
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("techreborn");
    }

}
