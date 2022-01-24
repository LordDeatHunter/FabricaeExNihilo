package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.*;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.util.Color;

public class TechRebornApiModule implements FabricaeExNihiloApiModule {

    @Override
    public void register(FENRegistries registries) {
        registries.registerOre("tin", Color.TIN, OreShape.NORMAL, OreMaterial.DIORITE);
        registries.registerOre("silver", Color.SILVER, OreShape.NORMAL, OreMaterial.STONE);
        registries.registerOre("lead", Color.LEAD, OreShape.COARSE, OreMaterial.STONE);
        registries.registerOre("iridium", Color.IRIDIUM, OreShape.FINE, OreMaterial.SAND);
        registries.registerOre("tungsten", Color.TUNGSTEN, OreShape.COARSE, OreMaterial.ENDSTONE);
        // No raw ores
        // registry.accept("aluminum", new OreDefinition(Color.ALUMINUM, OreDefinition.PieceShape.FINE, OreDefinition.BaseMaterial.SAND));
        // registry.accept("zinc", new OreDefinition(Color.ZINC, OreDefinition.PieceShape.FINE, OreDefinition.BaseMaterial.NETHERRACK));
        // registries.registerOre("platinum", Color.PLATINUM, OreShape.COARSE, OreMaterial.ENDSTONE);

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
