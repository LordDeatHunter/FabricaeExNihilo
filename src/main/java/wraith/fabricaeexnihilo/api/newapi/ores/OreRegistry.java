package wraith.fabricaeexnihilo.api.newapi.ores;

import java.util.HashMap;
import java.util.Map;

public class OreRegistry {
    private static final Map<String, OreDefinition> ORES = new HashMap<>();
    
    /**
     * Registers a new ore which is used to generate ore pieces and chunks
     * @param name The simple name of the ore. Should <strong>not</strong> be namespaced. Example: {@code tin}
     * @param def The definition of the properties of the ore.
     */
    public static void register(String name, OreDefinition def) {
        ORES.putIfAbsent(name, def);
    }
}
