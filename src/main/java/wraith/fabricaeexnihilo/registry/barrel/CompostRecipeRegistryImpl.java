package wraith.fabricaeexnihilo.registry.barrel;

import com.google.common.reflect.TypeToken;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.recipes.barrel.CompostRecipe;
import wraith.fabricaeexnihilo.api.registry.CompostRecipeRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.CompostCategory;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.REICompostRecipe;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompostRecipeRegistryImpl extends AbstractRegistry<List<CompostRecipe>> implements CompostRecipeRegistry {

    private final List<CompostRecipe> registry;

    public CompostRecipeRegistryImpl(List<CompostRecipe> registry) {
        this.registry = registry;
    }

    public CompostRecipeRegistryImpl() {
        this(new ArrayList<>());
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    protected List<CompostRecipe> serializable() {
        return registry;
    }

    @Override
    public boolean register(CompostRecipe recipe) {
        if (registry.stream().anyMatch(testRecipe -> testRecipe.ingredient() == recipe.ingredient())) {
            FabricaeExNihilo.LOGGER.warn("Conflicting Compost Recipe not registered: " + recipe);
            return false;
        }
        return registry.add(recipe);
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()) {
            try (var reader = new FileReader(file)) {
                List<CompostRecipe> json = gson.fromJson(
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
    public CompostRecipe getRecipe(@NotNull ItemStack stack) {
        return registry.stream().filter(recipe -> recipe.test(stack)).findFirst().orElse(null);
    }

    @Override
    public @NotNull List<REICompostRecipe> getREIRecipes() {
        return registry.stream()
                .map(recipe -> recipe.result().getItem())
                .collect(Collectors.toSet()).stream()
                .map(output -> {
                    // Grab all the same outputs
                    var sameRecipes = registry.stream().filter(recipe -> recipe.result().getItem() == output)
                            // Get a list of ALL possible inputs
                            .map(recipe -> recipe.ingredient().flatten().stream()
                                    .map(ingredient -> ItemUtils.asStack(ingredient, (int) Math.ceil(1.0 / recipe.amount())))
                                    .toList()
                            ).flatMap(List::stream).toList();
                    // Chunk the list up based on max number of inputs
                    var recipes = new ArrayList<REICompostRecipe>();
                    var temporaryRecipes = new ArrayList<ItemStack>();
                    int i = 0;
                    for (var sameRecipe : sameRecipes) {
                        if (i >= CompostCategory.MAX_INPUT) {
                            recipes.add(new REICompostRecipe(temporaryRecipes, ItemUtils.asStack(output)));
                            i = 0;
                            temporaryRecipes.clear();
                        }
                        temporaryRecipes.add(sameRecipe);
                        ++i;
                    }
                    return recipes;
                }).flatMap(List::stream).toList();
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<CompostRecipe>>() {}.getType();

    public static CompostRecipeRegistry fromJson(File file) {
        return fromJson(file, CompostRecipeRegistryImpl::new, MetaModule.INSTANCE::registerCompost);
    }

}
