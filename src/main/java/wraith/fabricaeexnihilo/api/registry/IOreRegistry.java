package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ore.EnumChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.EnumChunkShape;
import wraith.fabricaeexnihilo.modules.ore.EnumPieceShape;
import wraith.fabricaeexnihilo.modules.ore.OreProperties;
import wraith.fabricaeexnihilo.util.Color;

import java.util.List;

public interface IOreRegistry extends IRegistry<OreProperties> {
    List<OreProperties> getAll();

    boolean register(OreProperties... properties);
    default boolean register(String material, Color color, EnumPieceShape pieceShape, EnumChunkShape chunkShape, EnumChunkMaterial chunkMaterial) {
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
