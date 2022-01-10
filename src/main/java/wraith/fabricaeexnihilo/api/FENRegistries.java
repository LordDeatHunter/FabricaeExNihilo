package wraith.fabricaeexnihilo.api;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.util.Color;

//TODO: Document
public interface FENRegistries {
    void registerOre(String name, Color color, OreShape oreShape, OreMaterial baseMaterial);
    void registerMesh(String name, Color color, int enchantability);
    
    default void registerWood(String name) {
        registerSieve(name);
        registerBarrel(name, false);
        registerCrucible(name, false);
    }
    
    void registerSieve(String name);
    void registerCrucible(String name, boolean isStone);
    void registerBarrel(String name, boolean isStone);
    void registerInfestedLeaves(String name, Identifier source);
}
