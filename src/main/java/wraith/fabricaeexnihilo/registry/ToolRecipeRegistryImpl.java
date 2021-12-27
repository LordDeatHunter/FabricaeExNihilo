package wraith.fabricaeexnihilo.registry;

import com.google.common.reflect.TypeToken;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.api.crafting.Loot;
import wraith.fabricaeexnihilo.api.recipes.ToolRecipe;
import wraith.fabricaeexnihilo.api.registry.ToolRecipeRegistry;
import wraith.fabricaeexnihilo.compatibility.rei.tools.ToolCategory;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class ToolRecipeRegistryImpl extends AbstractRegistry<List<ToolRecipe>> implements ToolRecipeRegistry {

    private final List<ToolRecipe> registry = new ArrayList<>();

    public void clear() {
        registry.clear();
    }

    @Override
    public boolean register(ToolRecipe recipe) {
        var match = registry.stream().filter(entry -> recipe.ingredient() == entry.ingredient()).findFirst();
        if (match.isEmpty()) {
            return registry.add(recipe);
        } else
            match.get().loots().addAll(recipe.loots());
        // TODO use ToolManager to set break by tools
        return false;
    }

    @Override
    public boolean isRegistered(@NotNull ItemConvertible target) {
        return registry.stream().anyMatch(entry -> entry.ingredient().test(target.asItem()));
    }

    @Override
    public @NotNull List<ItemStack> getResult(@NotNull ItemConvertible target, @NotNull Random rand) {
        return getAllResults(target).stream().map(loot -> {
            var amount = loot.getChances().stream().filter(chance -> chance > rand.nextDouble()).count();
            return amount > 0 ? ItemUtils.stackWithCount(loot.getStack(), (int) amount) : ItemStack.EMPTY;
        }).filter(loot -> !loot.isEmpty()).toList();
    }

    @Override
    public @NotNull List<Loot> getAllResults(@NotNull ItemConvertible target) {
        return registry.stream()
                .filter(entry -> entry.ingredient().test(target.asItem()))
                .map(ToolRecipe::loots)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public void registerJson(File file) {
        try (var reader = new FileReader(file)) {
            List<ToolRecipe> json = gson.fromJson(reader, ToolRecipeRegistryImpl.SERIALIZATION_TYPE);
            json.forEach(entry -> register(entry.ingredient(), entry.loots()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ToolRecipe> serializable() {
        return registry;
    }

    @Override
    public @NotNull List<ToolRecipe> getREIRecipes() {
        return registry.stream().map(recipe -> {
            int i = 0;

            var toolRecipes = new ArrayList<ToolRecipe>();
            var tempLootables = new ArrayList<Loot>();

            for (var lootable : recipe.loots()) {
                if (i >= ToolCategory.MAX_OUTPUTS) {
                    toolRecipes.add(new ToolRecipe(recipe.ingredient(), tempLootables));
                    i = 0;
                    tempLootables.clear();
                }
                tempLootables.add(lootable);
                ++i;
            }
            if (!tempLootables.isEmpty()) {
                toolRecipes.add(new ToolRecipe(recipe.ingredient(), tempLootables));
            }
            return toolRecipes;
        }).flatMap(Collection::stream).toList();
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<ToolRecipe>>(){}.getType();

    public static ToolRecipeRegistry fromJson(File file, Consumer<ToolRecipeRegistryImpl> defaults) {
        return fromJson(file, ToolRecipeRegistryImpl::new, defaults);
    }

}