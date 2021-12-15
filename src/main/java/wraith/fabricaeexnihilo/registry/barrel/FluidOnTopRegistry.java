package wraith.fabricaeexnihilo.registry.barrel;

import com.google.common.reflect.TypeToken;
import net.minecraft.fluid.Fluid;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidOnTopRecipe;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FluidOnTopRegistry extends AbstractRegistry<List<FluidOnTopRecipe>> implements wraith.fabricaeexnihilo.api.registry.FluidOnTopRegistry {

    private final List<FluidOnTopRecipe> registry = new ArrayList<>();

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    protected List<FluidOnTopRecipe> serializable() {
        return registry;
    }

    @Override
    public boolean register(FluidOnTopRecipe recipe) {
        return registry.add(recipe);
    }

    @Override
    public BarrelMode getResult(Fluid contents, Fluid onTop) {
        return registry.stream().filter(recipe -> recipe.inBarrel().test(contents) && recipe.onTop().test(onTop)).findFirst().map(FluidOnTopRecipe::result).orElse(null);
    }

    @Override
    public List<FluidOnTopRecipe> getREIRecipes() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()){
            try (var reader = new FileReader(file)) {
                List<FluidOnTopRecipe> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<FluidOnTopRecipe>>(){}.getType();

    public static wraith.fabricaeexnihilo.api.registry.FluidOnTopRegistry fromJson(File file) {
        return fromJson(file, wraith.fabricaeexnihilo.registry.barrel.FluidOnTopRegistry::new, MetaModule.INSTANCE::registerFluidOnTop);
    }

}
