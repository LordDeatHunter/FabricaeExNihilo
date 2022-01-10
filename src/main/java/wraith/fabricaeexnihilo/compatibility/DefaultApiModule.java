package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.util.Color;

    /* TODO: More api modules:
            Modern Industrialisation: https://github.com/AztechMC/Modern-Industrialization/blob/master/src/main/java/aztech/modern_industrialization/materials/MIMaterials.java
            Industrial Revolution
            + any other mods that add ores or trees...
         */
public class DefaultApiModule implements FabricaeExNihiloApiModule {
    @Override
    public void register(FENRegistries registries) {
        registries.registerOre("iron", Color.IRON, OreShape.NORMAL, OreMaterial.GRANITE);
        registries.registerOre("gold", Color.GOLD, OreShape.FINE, OreMaterial.STONE);
        
        registries.registerWood("oak");
        registries.registerWood("birch");
        registries.registerWood("spruce");
        registries.registerWood("acacia");
        registries.registerWood("dark_oak");
        registries.registerWood("jungle");
        
        registries.registerMesh("string", Color.WHITE, 10);
        registries.registerMesh("flint", Color.GRAY, 12);
        registries.registerMesh("iron", new Color("777777"), 14);
        registries.registerMesh("gold", Color.GOLDEN, 22);
        registries.registerMesh("emerald", Color.DARK_GREEN, 24);
        registries.registerMesh("diamond", Color.DARK_AQUA, 10);
        registries.registerMesh("netherite", Color.BLACK, 15);
        
        registries.registerInfestedLeaves("oak", new Identifier("minecraft:oak_leaves"));
        registries.registerInfestedLeaves("birch", new Identifier("minecraft:birch_leaves"));
        registries.registerInfestedLeaves("spruce", new Identifier("minecraft:spruce_leaves"));
        registries.registerInfestedLeaves("acacia", new Identifier("minecraft:acacia_leaves"));
        registries.registerInfestedLeaves("dark_oak", new Identifier("minecraft:dark_oak_leaves"));
        registries.registerInfestedLeaves("jungle", new Identifier("minecraft:jungle_leaves"));
    }
}
