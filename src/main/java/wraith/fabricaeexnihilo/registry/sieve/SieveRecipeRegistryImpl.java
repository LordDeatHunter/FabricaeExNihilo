package wraith.fabricaeexnihilo.registry.sieve;

import com.google.common.reflect.TypeToken;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.api.recipes.SieveRecipe;
import wraith.fabricaeexnihilo.api.registry.SieveRecipeRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.compatibility.rei.sieve.SieveCategory;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SieveRecipeRegistryImpl extends AbstractRegistry<List<SieveRecipe>> implements SieveRecipeRegistry {

    private final List<SieveRecipe> registry;

    public SieveRecipeRegistryImpl() {
        this(new ArrayList<>());
    }

    public SieveRecipeRegistryImpl(List<SieveRecipe> registry) {
        this.registry = registry;
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    public List<ItemStack> getResult(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable, @Nullable PlayerEntity player, Random rand) {
        var allResults = getAllResults(mesh, fluid, sievable);
        var tries = 1;

        // Player's luck
        if (player != null) {
            tries += player.getLuck();
        }

        // Fortune tries
        if (FabricaeExNihilo.CONFIG.modules.sieves.fortune) {
            tries += EnchantmentHelper.get(mesh).getOrDefault(Enchantments.FORTUNE, 0);
        }

        var results = new ArrayList<ItemStack>();
        for (int i = 0; i < tries; ++i) {
            allResults.forEach(loot -> {
                var num = (int) loot.chances().stream().filter(chance -> rand.nextDouble() < chance).count();
                if (num > 0) {
                    var stack = loot.stack().copy();
                    stack.setCount(num);
                    results.add(stack);
                }
            });
        }

        return results;
    }

    @Override
    public List<Loot> getAllResults(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable) {
        return registry.stream()
                .filter(recipe -> recipe.test(mesh, fluid, sievable))
                .map(SieveRecipe::loot)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValidRecipe(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable) {
        return registry.stream().anyMatch(recipe -> recipe.test(mesh, fluid, sievable));
    }

    @Override
    public boolean isValidMesh(ItemStack mesh) {
        return registry.stream().anyMatch(recipe -> recipe.mesh().test(mesh));
    }

    @Override
    public boolean register(SieveRecipe recipe) {
        for (var sieveRecipe : registry) {
            if (sieveRecipe.test(recipe)) {
                sieveRecipe.loot().addAll(recipe.loot());
                return true;
            }
        }
        return registry.add(recipe);
    }

    @Override
    public Collection<SieveRecipe> getAllRecipes() {
        return registry;
    }

    @Override
    public Collection<SieveRecipe> getREIRecipes() {
        return registry.stream().map(recipe -> {
            var recipes = new ArrayList<SieveRecipe>();
            var temporaryLootables = new ArrayList<Loot>();
            int i = 0;
            for (var lootable : recipe.loot()) {
                if (i >= SieveCategory.MAX_OUTPUTS) {
                    recipes.add(new SieveRecipe(recipe.mesh(), recipe.fluid(), recipe.sievable(), temporaryLootables));
                    i = 0;
                    temporaryLootables.clear();
                }
                temporaryLootables.add(lootable);
                ++i;
            }
            return recipes;
        }).flatMap(List::stream).toList();
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()) {
            try {
                ArrayList<SieveRecipe> json = FabricaeExNihilo.RECIPE_GSON.fromJson(new FileReader(file), SERIALIZATION_TYPE);
                json.forEach(this::register);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<SieveRecipe> serializable() {
        return registry;
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<SieveRecipe>>() {}.getType();

    public static SieveRecipeRegistryImpl fromJson(File file) {
        return fromJson(
                file,
                SieveRecipeRegistryImpl::new,
                MetaModule.INSTANCE::registerSieve
        );
    }

}
