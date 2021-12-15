package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.client.util.ModelIdentifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ore.ChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.ChunkShape;
import wraith.fabricaeexnihilo.modules.ore.PieceShape;
import wraith.fabricaeexnihilo.modules.ore.OreProperties;
import wraith.fabricaeexnihilo.util.Color;

import java.util.List;

public interface OreRegistry extends Registry<OreProperties> {
    List<OreProperties> getAll();

    boolean register(OreProperties... properties);
    default boolean register(String material, Color color, PieceShape pieceShape, ChunkShape chunkShape, ChunkMaterial chunkMaterial) {
        var oreProperties =
                OreProperties.Builder(material)
                        .setColor(color)
                        .setPieceShape(pieceShape)
                        .setChunkShape(chunkShape)
                        .setChunkMaterial(chunkMaterial)
                .build();
        return register(oreProperties);
    }

    void registerPieceItems();
    void registerChunkItems();

    @Nullable
    OreProperties getPropertiesForModel(ModelIdentifier identifier);
}
