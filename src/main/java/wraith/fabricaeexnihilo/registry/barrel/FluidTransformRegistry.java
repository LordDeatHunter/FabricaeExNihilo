package wraith.fabricaeexnihilo.registry.barrel;

import com.google.common.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidTransformRecipe;
import wraith.fabricaeexnihilo.api.registry.IFluidTransformRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FluidTransformRegistry extends AbstractRegistry<List<FluidTransformRecipe>> implements IFluidTransformRegistry {

    private final List<FluidTransformRecipe> registry;

    public FluidTransformRegistry(List<FluidTransformRecipe> registry) {
        this.registry = registry;
    }

    public FluidTransformRegistry() {
        this(new ArrayList<>());
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    public boolean register(FluidTransformRecipe recipe) {
        return registry.add(recipe);
    }

    @Override
    public BarrelMode getResult(Fluid fluid, Block block) {
        var match = registry.stream().filter(recipe -> recipe.inBarrel().test(fluid) && recipe.catalyst().test(block)).findFirst().orElse(null);
        return match == null ? null : match.result();
    }

    @Override
    public Collection<FluidTransformRecipe> getREIRecipes() {
        return registry;
    }

    @Override
    protected List<FluidTransformRecipe> serializable() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()){
            try (var reader = new FileReader(file)) {
                List<FluidTransformRecipe> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<FluidTransformRecipe>>(){}.getType();

    public static IFluidTransformRegistry fromJson(File file) {
        return fromJson(file, FluidTransformRegistry::new, MetaModule.INSTANCE::registerFluidTransform);
    }

}