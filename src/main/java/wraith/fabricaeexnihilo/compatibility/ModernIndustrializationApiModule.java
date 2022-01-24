package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.util.Color;

public class ModernIndustrializationApiModule implements FabricaeExNihiloApiModule {

    @Override
    public void register(FENRegistries registries) {
        registries.registerOre("antimony", new Color("#C5C5D6"), OreShape.NORMAL, OreMaterial.ANDESITE);
        registries.registerOre("iridium", Color.IRIDIUM, OreShape.NORMAL, OreMaterial.ANDESITE);
        registries.registerOre("tin", Color.TIN, OreShape.NORMAL, OreMaterial.DIORITE);
        registries.registerOre("silver", Color.SILVER, OreShape.NORMAL, OreMaterial.STONE);
        registries.registerOre("lead", Color.LEAD, OreShape.COARSE, OreMaterial.STONE);
        registries.registerOre("nickel", Color.NICKEL, OreShape.COARSE, OreMaterial.ANDESITE);
        registries.registerOre("platinum", Color.PLATINUM, OreShape.NORMAL, OreMaterial.STONE);
        registries.registerOre("titanium", Color.TITANIUM, OreShape.COARSE, OreMaterial.GRANITE);
        registries.registerOre("tungsten", Color.TUNGSTEN, OreShape.COARSE, OreMaterial.ENDSTONE);
        registries.registerOre("uranium", Color.URANIUM, OreShape.COARSE, OreMaterial.ANDESITE);
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("modern_industrialization");
    }

}
