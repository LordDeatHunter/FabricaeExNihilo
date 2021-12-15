package wraith.fabricaeexnihilo.registry.witchwater;

import com.google.common.reflect.TypeToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterEntityRecipe;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WitchWaterEntityRegistry extends AbstractRegistry<List<WitchWaterEntityRecipe>> implements wraith.fabricaeexnihilo.api.registry.WitchWaterEntityRegistry {

    private final List<WitchWaterEntityRecipe> registry;

    public WitchWaterEntityRegistry(List<WitchWaterEntityRecipe> registry) {
        this.registry = registry;
    }

    public WitchWaterEntityRegistry() {
        this(new ArrayList<>());
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    protected List<WitchWaterEntityRecipe> serializable() {
        return registry;
    }

    @Override
    public boolean register(WitchWaterEntityRecipe recipe) {
        return registry.add(recipe);
    }

    @Nullable
    @Override
    public EntityType<?> getSpawn(Entity entity) {
        return registry.stream().filter(recipe -> recipe.test(entity)).findFirst().map(WitchWaterEntityRecipe::toSpawn).orElse(null);
    }

    @Override
    public List<WitchWaterEntityRecipe> getAll() {
        return registry;
    }

    @Override
    public Collection<WitchWaterEntityRecipe> getREIRecipes() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()) {
            try (var reader = new FileReader(file)) {
                List<WitchWaterEntityRecipe> json = gson.fromJson(reader, SERIALIZATION_TYPE);
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<WitchWaterEntityRecipe>>() {}.getType();

    public static wraith.fabricaeexnihilo.api.registry.WitchWaterEntityRegistry fromJson(File file) {
        return fromJson(file, wraith.fabricaeexnihilo.registry.witchwater.WitchWaterEntityRegistry::new, MetaModule.INSTANCE::registerWitchWaterEntity);
    }

}
