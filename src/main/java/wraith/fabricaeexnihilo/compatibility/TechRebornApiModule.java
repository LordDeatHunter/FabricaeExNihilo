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
    public void registerOres(BiConsumer<Identifier, OreDefinition> registry) {
        // Overworld ores
        registry.accept(id("tin"), new OreDefinition(Color.TIN, OreDefinition.PieceShape.NORMAL, OreDefinition.ChunkShape.LUMP, OreDefinition.BaseMaterial.DIORITE));
        registry.accept(id("silver"), new OreDefinition(Color.SILVER, OreDefinition.PieceShape.NORMAL, OreDefinition.ChunkShape.CHUNK, OreDefinition.BaseMaterial.STONE));
        registry.accept(id("lead"), new OreDefinition(Color.LEAD, OreDefinition.PieceShape.COARSE, OreDefinition.ChunkShape.LUMP, OreDefinition.BaseMaterial.STONE));
        registry.accept(id("aluminum"), new OreDefinition(Color.ALUMINUM, OreDefinition.PieceShape.FINE, OreDefinition.ChunkShape.CHUNK, OreDefinition.BaseMaterial.SAND));
        registry.accept(id("iridium"), new OreDefinition(Color.IRIDIUM, OreDefinition.PieceShape.FINE, OreDefinition.ChunkShape.CHUNK, OreDefinition.BaseMaterial.SAND));
        
        // Nether ores
        registry.accept(id("zinc"), new OreDefinition(Color.ZINC, OreDefinition.PieceShape.FINE, OreDefinition.ChunkShape.FLINT, OreDefinition.BaseMaterial.NETHERRACK));
        
        // End ores
        registry.accept(id("tungsten"), new OreDefinition(Color.TUNGSTEN, OreDefinition.PieceShape.COARSE, OreDefinition.ChunkShape.CHUNK, OreDefinition.BaseMaterial.ENDSTONE));
        registry.accept(id("platinum"), new OreDefinition(Color.PLATINUM, OreDefinition.PieceShape.COARSE, OreDefinition.ChunkShape.CHUNK, OreDefinition.BaseMaterial.ENDSTONE));
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
