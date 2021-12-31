package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.MeshDefinition;
import wraith.fabricaeexnihilo.api.OreDefinition;
import wraith.fabricaeexnihilo.modules.ore.ChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.ChunkShape;
import wraith.fabricaeexnihilo.modules.ore.PieceShape;
import wraith.fabricaeexnihilo.util.Color;

import java.util.function.BiConsumer;

public class TechRebornApiModule implements FabricaeExNihiloApiModule {
    @Override
    public void registerOres(BiConsumer<String, OreDefinition> registry) {
        // Overworld ores
        registry.accept("tin", new OreDefinition(Color.TIN, PieceShape.NORMAL, ChunkShape.LUMP, ChunkMaterial.DIORITE));
        registry.accept("silver", new OreDefinition(Color.SILVER, PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.STONE));
        registry.accept("lead", new OreDefinition(Color.LEAD, PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.STONE));
        registry.accept("aluminum", new OreDefinition(Color.ALUMINUM, PieceShape.FINE, ChunkShape.CHUNK, ChunkMaterial.SAND));
        registry.accept("iridium", new OreDefinition(Color.IRIDIUM, PieceShape.FINE, ChunkShape.CHUNK, ChunkMaterial.SAND));

        // Nether ores
        registry.accept("zinc", new OreDefinition(Color.ZINC, PieceShape.FINE, ChunkShape.FLINT, ChunkMaterial.NETHERRACK));

        // End ores
        registry.accept("tungsten", new OreDefinition(Color.TUNGSTEN, PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.ENDSTONE));
        registry.accept("platinum", new OreDefinition(Color.PLATINUM, PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.ENDSTONE));
    }
    
    @Override
    public void registerMeshes(BiConsumer<Identifier, MeshDefinition> registry) {
        registry.accept(new Identifier("techreborn", "carbon_mesh"), new MeshDefinition(Color.BLACK, 14));
    }
    
    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("techreborn");
    }
}
