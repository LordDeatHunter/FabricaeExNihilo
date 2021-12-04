package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.sieves.MeshProperties;
import wraith.fabricaeexnihilo.util.Color;

import java.util.List;

public interface IMeshRegistry extends IRegistry<MeshProperties> {

    default boolean register(Identifier identifier, int enchantability, String displayName, Color color, Identifier ingredient) {
        return register(new MeshProperties(identifier, enchantability, displayName, color, ingredient));
    }

    void registerItems();

    List<MeshProperties> getAll();

}