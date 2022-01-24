package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.util.Color;

public class IndustrialRevolutionApiModule implements FabricaeExNihiloApiModule {

    @Override
    public void register(FENRegistries registries) {
        registries.registerOre("tin", Color.TIN, OreShape.NORMAL, OreMaterial.DIORITE);
        registries.registerOre("silver", Color.SILVER, OreShape.NORMAL, OreMaterial.STONE);
        registries.registerOre("lead", Color.LEAD, OreShape.COARSE, OreMaterial.STONE);
        registries.registerOre("tungsten", Color.TUNGSTEN, OreShape.COARSE, OreMaterial.ENDSTONE);
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("indrev");
    }

}
