package wraith.fabricaeexnihilo.registry.witchwater;

import com.google.common.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.WeightedList;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterWorldRecipe;
import wraith.fabricaeexnihilo.api.registry.IWitchWaterWorldRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterWorldCategory;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class WitchWaterWorldRegistry extends AbstractRegistry<List<WitchWaterWorldRecipe>> implements IWitchWaterWorldRegistry {

    private final List<WitchWaterWorldRecipe> registry;

    public WitchWaterWorldRegistry(List<WitchWaterWorldRecipe> registry) {
        this.registry = registry;
    }

    public WitchWaterWorldRegistry() {
        this(new ArrayList<>());
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    protected List<WitchWaterWorldRecipe> serializable() {
        return registry;
    }

    @Override
    public boolean register(WitchWaterWorldRecipe recipe) {
        var match = registry.stream().filter(witchRecipe -> witchRecipe.fluid() == recipe.fluid()).findFirst();
        if(match.isPresent()) {
            match.get().results().amend(recipe.results());
            return false;
        }
        return registry.add(recipe);
    }

    @Override
    public boolean isRegistered(Fluid fluid) {
        return registry.stream().anyMatch(recipe -> recipe.fluid().test(fluid));
    }

    @Nullable
    @Override
    public Block getResult(Fluid fluid, Random rand) {
        var results = getAllResults(fluid);
        return results == null ? null : results.choose(rand);
    }

    @Nullable
    @Override
    public WeightedList getAllResults(Fluid fluid) {
        for (var entry : registry) {
            if(entry.fluid().test(fluid)) {
                return entry.results();
            }
        }
        return null;
    }

    @Override
    public Collection<WitchWaterWorldRecipe> getREIRecipes() {
        return registry.stream().map(recipe -> {
            int i = 0;

            var toolRecipes = new ArrayList<WitchWaterWorldRecipe>();
            var tempRecipes = new HashMap<Block, Integer>();

            for (var entry : recipe.results().getValues().entrySet()) {
                if (i >= WitchWaterWorldCategory.MAX_OUTPUTS) {
                    toolRecipes.add(new WitchWaterWorldRecipe(recipe.fluid(), new WeightedList(tempRecipes)));
                    i = 0;
                    tempRecipes.clear();
                }
                tempRecipes.put(entry.getKey(), entry.getValue());
                ++i;
            }
            if (!tempRecipes.isEmpty()) {
                toolRecipes.add(new WitchWaterWorldRecipe(recipe.fluid(), new WeightedList(tempRecipes)));
            }
            return toolRecipes;
        }).flatMap(Collection::stream).toList();
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()) {
            try (var reader = new FileReader(file)) {
                List<WitchWaterWorldRecipe> json = gson.fromJson(reader, SERIALIZATION_TYPE);
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<WitchWaterWorldRecipe>>() {}.getType();

    // TODO fix serialization
    public static IWitchWaterWorldRegistry fromJson(File file) {
        return fromJson(file, WitchWaterWorldRegistry::new, MetaModule.INSTANCE::registerWitchWaterWorld);
    }

}
