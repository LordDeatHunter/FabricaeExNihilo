package wraith.fabricaeexnihilo.registry.barrel;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.reflect.TypeToken;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.recipes.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.api.registry.AlchemyRecipeRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AlchemyRecipeRegistryImpl extends AbstractRegistry<List<AlchemyRecipe>> implements AlchemyRecipeRegistry {

    private final List<AlchemyRecipe> registry;

    public AlchemyRecipeRegistryImpl(List<AlchemyRecipe> registry) {
        this.registry = registry;
    }

    public AlchemyRecipeRegistryImpl() {
        this(new ArrayList<>());
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    protected List<AlchemyRecipe> serializable() {
        return registry;
    }

    @Override
    public boolean register(AlchemyRecipe recipe) {
        if(registry.stream().anyMatch(alchemyRecipe -> alchemyRecipe.match(recipe))) {
            FabricaeExNihilo.LOGGER.warn("Conflicting Alchemy Recipe not registered: " + recipe);
            return false;
        }
        return registry.add(recipe);
    }

    @Nullable
    @Override
    public AlchemyRecipe getRecipe(@NotNull FluidVolume reactant, @NotNull ItemStack catalyst) {
        return registry.stream().filter(recipe -> recipe.test(reactant, catalyst)).findFirst().orElse(null);
    }

    @Override
    public @NotNull List<AlchemyRecipe> getREIRecipes() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()){
            try (var reader = new FileReader(file)) {
                List<AlchemyRecipe> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<AlchemyRecipe>>(){}.getType();

    public static AlchemyRecipeRegistry fromJson(File file) {
        return fromJson(file, AlchemyRecipeRegistryImpl::new, MetaModule.INSTANCE::registerAlchemy);
    }

}
