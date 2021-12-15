package wraith.fabricaeexnihilo.registry.barrel;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.reflect.TypeToken;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.recipes.barrel.MilkingRecipe;
import wraith.fabricaeexnihilo.api.registry.MilkingRecipeRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MilkingRecipeRegistryImpl extends AbstractRegistry<List<MilkingRecipe>> implements MilkingRecipeRegistry {

    private final List<MilkingRecipe> registry;

    public MilkingRecipeRegistryImpl(List<MilkingRecipe> registry) {
        this.registry = registry;
    }

    public MilkingRecipeRegistryImpl() {
        this(new ArrayList<>());
    }

    @Override
    protected List<MilkingRecipe> serializable() {
        return registry;
    }

    @Override
    public void clear() {
        registry.clear();
    }


    @Override
    public boolean register(MilkingRecipe recipe) {
        if (registry.stream().anyMatch(milkingRecipe -> milkingRecipe.entity() == recipe.entity())) {
            FabricaeExNihilo.LOGGER.warn("Conflicting Milking Recipe not registered: " + recipe);
            return false;
        }
        return registry.add(recipe);
    }

    @Override
    public Pair<FluidVolume, Integer> getResult(Entity entity) {
        var match = registry.stream().filter(recipe -> recipe.entity().test(entity)).findFirst().orElse(null);
        return match == null ? null : new Pair<>(match.result(), match.cooldown());
    }

    @Override
    public boolean hasResult(Entity entity) {
        return registry.stream().anyMatch(recipe -> recipe.entity().test(entity));
    }

    @Override
    public List<MilkingRecipe> getREIRecipes() {
        return registry;
    }

    public void registerJson(File file) {
        if (file.exists()) {
            try (var reader = new FileReader(file)) {
                List<MilkingRecipe> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<MilkingRecipe>>(){}.getType();

    public static MilkingRecipeRegistry fromJson(File file) {
        return fromJson(file, MilkingRecipeRegistryImpl::new, MetaModule.INSTANCE::registerMilking);
    }

}