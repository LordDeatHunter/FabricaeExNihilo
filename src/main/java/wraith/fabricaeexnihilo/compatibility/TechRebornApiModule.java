package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.api.newapi.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.newapi.ores.OreDefinition;
import wraith.fabricaeexnihilo.modules.ore.ChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.ChunkShape;
import wraith.fabricaeexnihilo.modules.ore.PieceShape;
import wraith.fabricaeexnihilo.util.Color;

import java.util.function.BiConsumer;

public class TechRebornApiModule implements FabricaeExNihiloApiModule {
    @Override
    public void registerOres(BiConsumer<String, OreDefinition> registry) {
        // Overworld ores
        // TODO: add galena, iridium and bauxite
        registry.accept("tin", new OreDefinition(Color.TIN, PieceShape.NORMAL, ChunkShape.LUMP, ChunkMaterial.DIORITE));
        registry.accept("silver", new OreDefinition(Color.SILVER, PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.STONE));
        registry.accept("lead", new OreDefinition(Color.LEAD,  PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.STONE));
    
        // Nether ores
        // TODO: add pyrite, cinnabar and sphalerite
    
        // End ores
        // TODO: add sheldonite and sodalite
        registry.accept("tungsten", new OreDefinition(Color.TUNGSTEN, PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.ENDSTONE));
    }
    
    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("techreborn");
    }
}
