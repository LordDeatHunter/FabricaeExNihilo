package wraith.fabricaeexnihilo.registry.sieve;

import com.google.common.reflect.TypeToken;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.registry.IOreRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.modules.ore.OreChunkItem;
import wraith.fabricaeexnihilo.modules.ore.OrePieceItem;
import wraith.fabricaeexnihilo.modules.ore.OreProperties;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OreRegistry extends AbstractRegistry<List<OreProperties>> implements IOreRegistry {

    private static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(64);
    private final List<OreProperties> registry;

    public OreRegistry(List<OreProperties> registry) {
        this.registry = registry;
    }

    public OreRegistry() {
        this(new ArrayList<>());
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    public List<OreProperties> getAll() {
        return registry;
    }

    @Override
    public boolean register(OreProperties recipe) {
        return registry.add(recipe);
    }

    @Override
    public boolean register(OreProperties... properties) {
        return registry.addAll(Arrays.asList(properties));
    }

    @Override
    public void registerPieceItems() {
        registry.forEach(entry -> Registry.register(Registry.ITEM, entry.getPieceID(), new OrePieceItem(entry, ITEM_SETTINGS)));
    }

    @Override
    public void registerChunkItems() {
        registry.forEach(entry -> Registry.register(Registry.ITEM, entry.getChunkID(), new OreChunkItem(entry, ITEM_SETTINGS)));
    }

    @Nullable
    @Override
    public OreProperties getPropertiesForModel(ModelIdentifier identifier) {
        return registry.stream()
                .filter(
                        entry -> entry.getChunkID().getPath().equals(identifier.getPath()) ||
                                entry.getPieceID().getPath().equals(identifier.getPath()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<OreProperties> serializable() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()) {
            try {
                ArrayList<OreProperties> json = gson.fromJson(new FileReader(file), SERIALIZATION_TYPE);
                json.forEach(this::register);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<OreProperties>>() {}.getType();

    public static IOreRegistry fromJson(File file) {
        return fromJson(file, OreRegistry::new, MetaModule.INSTANCE::registerOres);
    }

}
