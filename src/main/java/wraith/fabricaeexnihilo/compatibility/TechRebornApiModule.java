package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

import static wraith.fabricaeexnihilo.modules.ModBlocks.infestedLeavesSettings;
import static wraith.fabricaeexnihilo.modules.ModBlocks.woodSettings;
import static wraith.fabricaeexnihilo.modules.ModItems.itemSettings;

public class TechRebornApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("tin", itemSettings());
        registries.registerOrePiece("silver", itemSettings());
        registries.registerOrePiece("lead", itemSettings());
        registries.registerOrePiece("iridium", itemSettings());
        registries.registerOrePiece("tungsten", itemSettings());
        // No raw ores
        // registry.accept("aluminum", new OreDefinition(Color.ALUMINUM, OreDefinition.PieceShape.FINE, OreDefinition.BaseMaterial.SAND));
        // registry.accept("zinc", new OreDefinition(Color.ZINC, OreDefinition.PieceShape.FINE, OreDefinition.BaseMaterial.NETHERRACK));
        // registries.registerOrePiece("platinum", Color.PLATINUM);

        registries.registerMesh("carbon", Color.BLACK, 14, itemSettings());
        registries.registerWood("rubber", false, woodSettings());
        registries.registerInfestedLeaves("rubber", new Identifier("techreborn:rubber_leaves"), infestedLeavesSettings());
        //TODO: remove with seed refactor
        registries.registerSeed("rubber", new Identifier("techreborn:rubber_sapling"));
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("techreborn");
    }

}
