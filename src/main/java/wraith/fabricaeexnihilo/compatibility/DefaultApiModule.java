package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.MeshDefinition;
import wraith.fabricaeexnihilo.api.OreDefinition;
import wraith.fabricaeexnihilo.api.OreDefinition.BaseMaterial;
import wraith.fabricaeexnihilo.api.OreDefinition.ChunkShape;
import wraith.fabricaeexnihilo.api.OreDefinition.PieceShape;
import wraith.fabricaeexnihilo.util.Color;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class DefaultApiModule implements FabricaeExNihiloApiModule {
    /* TODO: More api modules:
        Modern Industrialisation: https://github.com/AztechMC/Modern-Industrialization/blob/master/src/main/java/aztech/modern_industrialization/materials/MIMaterials.java
        Industrial Revolution
        + any other mods that add ores or trees...
     */
    @Override
    public void registerOres(BiConsumer<Identifier, OreDefinition> registry) {
        registry.accept(id("iron"), new OreDefinition(Color.IRON, PieceShape.NORMAL, ChunkShape.CHUNK, BaseMaterial.GRANITE));
        registry.accept(id("gold"), new OreDefinition(Color.GOLD, PieceShape.FINE, ChunkShape.CHUNK, BaseMaterial.STONE));
    }
    
    @Override
    public void registerWoods(Consumer<Identifier> registry) {
        registry.accept(id("oak"));
        registry.accept(id("birch"));
        registry.accept(id("spruce"));
        registry.accept(id("acacia"));
        registry.accept(id("dark_oak"));
        registry.accept(id("jungle"));
    }
    
    @Override
    public void registerMeshes(BiConsumer<Identifier, MeshDefinition> registry) {
        registry.accept(id("string_mesh"), new MeshDefinition(Color.WHITE, 10));
        registry.accept(id("flint_mesh"), new MeshDefinition(Color.GRAY, 12));
        registry.accept(id("iron_mesh"), new MeshDefinition(new Color("777777"), 14));
        
        registry.accept(id("gold_mesh"), new MeshDefinition(Color.GOLDEN, 22));
        registry.accept(id("emerald_mesh"), new MeshDefinition(Color.DARK_GREEN, 24));
        
        registry.accept(id("diamond_mesh"), new MeshDefinition(Color.DARK_AQUA, 10));
        registry.accept(id("netherite_mesh"), new MeshDefinition(Color.DARK_GRAY, 15));
    }
    
    @Override
    public void registerInfestedLeaves(BiConsumer<Identifier, Identifier> registry) {
        registry.accept(new Identifier("minecraft:oak_leaves"), id("oak_leaves"));
        registry.accept(new Identifier("minecraft:birch_leaves"), id("birch_leaves"));
        registry.accept(new Identifier("minecraft:spruce_leaves"), id("spruce_leaves"));
        registry.accept(new Identifier("minecraft:acacia_leaves"), id("acacia_leaves"));
        registry.accept(new Identifier("minecraft:dark_oak_leaves"), id("dark_oak_leaves"));
        registry.accept(new Identifier("minecraft:jungle_leaves"), id("jungle_leaves"));
    }
}
