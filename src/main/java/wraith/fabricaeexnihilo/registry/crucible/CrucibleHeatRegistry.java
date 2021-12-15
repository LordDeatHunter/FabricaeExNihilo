package wraith.fabricaeexnihilo.registry.crucible;

import com.google.common.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CrucibleHeatRegistry extends AbstractRegistry<List<CrucibleHeatRecipe>> implements wraith.fabricaeexnihilo.api.registry.CrucibleHeatRegistry {

    private final List<CrucibleHeatRecipe> registry;

    public CrucibleHeatRegistry() {
        this(new ArrayList<>());
    }

    public CrucibleHeatRegistry(List<CrucibleHeatRecipe> registry) {
        this.registry = registry;
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    public boolean register(CrucibleHeatRecipe recipe) {
        return registry.add(recipe);
    }

    @Override
    public int getHeat(Block block) {
        return registry.stream().filter(recipe -> recipe.test(block)).findFirst().map(CrucibleHeatRecipe::value).orElse(0);
    }

    @Override
    public int getHeat(Fluid fluid) {
        return registry.stream().filter(recipe -> recipe.test(fluid)).findFirst().map(CrucibleHeatRecipe::value).orElse(0);
    }

    @Override
    public int getHeat(BlockItem item) {
        return registry.stream().filter(recipe -> recipe.test(item)).findFirst().map(CrucibleHeatRecipe::value).orElse(0);
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()){
            try (var reader = new FileReader(file)) {
                List<CrucibleHeatRecipe> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<CrucibleHeatRecipe> serializable() {
        return registry;
    }

    @Override
    public Collection<CrucibleHeatRecipe> getREIRecipes() {
        return registry;
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<CrucibleHeatRecipe>>(){}.getType();

    public static wraith.fabricaeexnihilo.api.registry.CrucibleHeatRegistry fromJson(File file) {
        return fromJson(file, wraith.fabricaeexnihilo.registry.crucible.CrucibleHeatRegistry::new, MetaModule.INSTANCE::registerCrucibleHeat);
    }

}