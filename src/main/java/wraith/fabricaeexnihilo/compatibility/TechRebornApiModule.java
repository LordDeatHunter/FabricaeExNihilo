package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.MeshDefinition;
import wraith.fabricaeexnihilo.api.OreDefinition;
import wraith.fabricaeexnihilo.util.Color;

import java.util.function.BiConsumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class TechRebornApiModule implements FabricaeExNihiloApiModule {
    @Override
    public void registerOres(BiConsumer<String, OreDefinition> registry) {
        // Overworld ores
        registry.accept("tin", new OreDefinition(Color.TIN, OreDefinition.PieceShape.NORMAL, OreDefinition.ChunkShape.LUMP, OreDefinition.ChunkMaterial.DIORITE));
        registry.accept("silver", new OreDefinition(Color.SILVER, OreDefinition.PieceShape.NORMAL, OreDefinition.ChunkShape.CHUNK, OreDefinition.ChunkMaterial.STONE));
        registry.accept("lead", new OreDefinition(Color.LEAD, OreDefinition.PieceShape.COARSE, OreDefinition.ChunkShape.LUMP, OreDefinition.ChunkMaterial.STONE));
        registry.accept("aluminum", new OreDefinition(Color.ALUMINUM, OreDefinition.PieceShape.FINE, OreDefinition.ChunkShape.CHUNK, OreDefinition.ChunkMaterial.SAND));
        registry.accept("iridium", new OreDefinition(Color.IRIDIUM, OreDefinition.PieceShape.FINE, OreDefinition.ChunkShape.CHUNK, OreDefinition.ChunkMaterial.SAND));
        
        // Nether ores
        registry.accept("zinc", new OreDefinition(Color.ZINC, OreDefinition.PieceShape.FINE, OreDefinition.ChunkShape.FLINT, OreDefinition.ChunkMaterial.NETHERRACK));
        
        // End ores
        registry.accept("tungsten", new OreDefinition(Color.TUNGSTEN, OreDefinition.PieceShape.COARSE, OreDefinition.ChunkShape.CHUNK, OreDefinition.ChunkMaterial.ENDSTONE));
        registry.accept("platinum", new OreDefinition(Color.PLATINUM, OreDefinition.PieceShape.COARSE, OreDefinition.ChunkShape.CHUNK, OreDefinition.ChunkMaterial.ENDSTONE));
    }
    
    @Override
    public void registerMeshes(BiConsumer<Identifier, MeshDefinition> registry) {
        registry.accept(id("carbon_mesh"), new MeshDefinition(Color.BLACK, 14));
    }
    
    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("techreborn");
    }
}
