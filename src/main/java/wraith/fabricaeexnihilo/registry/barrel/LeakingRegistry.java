package wraith.fabricaeexnihilo.registry.barrel;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.recipes.barrel.LeakingRecipe;
import wraith.fabricaeexnihilo.api.registry.ILeakingRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LeakingRegistry extends AbstractRegistry<List<LeakingRecipe>> implements ILeakingRegistry {

    private final List<LeakingRecipe> registry = new ArrayList<>();

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    protected List<LeakingRecipe> serializable() {
        return registry;
    }

    @Override
    public boolean register(LeakingRecipe recipe) {
        if(registry.stream().anyMatch(leakingRecipe -> leakingRecipe.fluid() == recipe.fluid() && leakingRecipe.target() == recipe.target())) {
            FabricaeExNihilo.LOGGER.warn("Conflicting Leaking Recipe not registered: " + recipe);
            return false;
        }
        return registry.add(recipe);
    }

    @Override
    public Pair<Block, FluidAmount> getResult(Block block, FluidVolume fluid) {
        var match = registry.stream().filter(recipe ->
                recipe.target().test(block) &&
                recipe.fluid().test(fluid) &&
                recipe.loss().isLessThanOrEqual(fluid.amount())).findFirst();
        if (match.isEmpty()) {
            return null;
        }
        var recipe = match.get();
        return new Pair<>(recipe.result(), recipe.loss());
    }

    @Override
    public List<LeakingRecipe> getREIRecipes() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()){
            try (var reader = new FileReader(file)) {
                List<LeakingRecipe> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<LeakingRecipe>>(){}.getType();

    public static ILeakingRegistry fromJson(File file) {
        return fromJson(file, LeakingRegistry::new, MetaModule.INSTANCE::registerLeaking);
    }

}
