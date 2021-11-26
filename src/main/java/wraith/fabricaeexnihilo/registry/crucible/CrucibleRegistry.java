package wraith.fabricaeexnihilo.registry.crucible;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.reflect.TypeToken;
import net.minecraft.item.Item;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.api.registry.ICrucibleRegistry;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class CrucibleRegistry extends AbstractRegistry<List<CrucibleRecipe>> implements ICrucibleRegistry {

    private final List<CrucibleRecipe> registry;

    public CrucibleRegistry() {
        this(new ArrayList<>());
    }

    public CrucibleRegistry(List<CrucibleRecipe> registry) {
        this.registry = registry;
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    public boolean register(CrucibleRecipe recipe) {
        return registry.add(recipe);
    }

    @Override
    public FluidVolume getResult(Item item) {
        return registry.stream().filter(recipe -> recipe.input().test(ItemUtils.asStack(item))).findFirst().map(CrucibleRecipe::output).orElse(null);
    }

    @Override
    protected List<CrucibleRecipe> serializable() {
        return registry;
    }

    @Override
    public Collection<CrucibleRecipe> getREIRecipes() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()){
            try (var reader = new FileReader(file)) {
                List<CrucibleRecipe> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<CrucibleRecipe>>(){}.getType();

    public static ICrucibleRegistry fromJson(File file, Consumer<CrucibleRegistry> defaults) {
        return fromJson(file, CrucibleRegistry::new, defaults);
    }

}