package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.item.Items;
import wraith.fabricaeexnihilo.api.newapi.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.newapi.ores.OreDefinition;
import wraith.fabricaeexnihilo.modules.ore.ChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.ChunkShape;
import wraith.fabricaeexnihilo.modules.ore.PieceShape;
import wraith.fabricaeexnihilo.util.Color;

import java.util.function.BiConsumer;

public class DefaultApiModule implements FabricaeExNihiloApiModule {
    /* TODO: More api modules:
        Modern Industrialisation: https://github.com/AztechMC/Modern-Industrialization/blob/master/src/main/java/aztech/modern_industrialization/materials/MIMaterials.java
        Industrial Revolution
        + any other mods that add ores or trees...
     */
    @Override
    public void registerOres(BiConsumer<String, OreDefinition> registry) {
        registry.accept("iron", new OreDefinition(Color.IRON, PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.GRANITE));
        registry.accept("gold", new OreDefinition(Color.GOLD, PieceShape.FINE, ChunkShape.CHUNK, ChunkMaterial.STONE));
    }
}
