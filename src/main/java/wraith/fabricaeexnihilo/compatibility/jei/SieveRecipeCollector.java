package wraith.fabricaeexnihilo.compatibility.jei;

import net.fabricmc.fabric.api.recipe.v1.FabricRecipeManager;
import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds JEI sieve recipe pages from the recipes the client actually knows about.
 *
 * <p>Since 1.21.2 vanilla no longer syncs full recipes to the client. FEN opts its
 * sieve serializer into Fabric's recipe synchronisation in
 * {@link FabricaeExNihilo#onInitialize()}, so the client receives the real server
 * recipes and JEI can answer reverse lookups such as "which mesh produces raw iron
 * pieces?".
 *
 * <p>Fabric's custom recipe sync packet arrives on a separate channel after the
 * vanilla recipe packet that restarts JEI, so {@link #onRecipesSynchronized} keeps
 * the latest snapshot and {@link FENJeiPlugin} rebuilds the category when it does.
 * An empty snapshot is never authoritative: every path must fall through to the
 * integrated-server fallback instead of returning early.
 */
public final class SieveRecipeCollector {

    private static volatile SynchronizedRecipes synchronizedRecipes;

    private SieveRecipeCollector() {
    }

    /** Called from FENJeiPlugin's ClientRecipeSynchronizedEvent listener. */
    public static void onRecipesSynchronized(SynchronizedRecipes recipes) {
        synchronizedRecipes = recipes;
    }

    public static List<JeiSieveRecipe> collect() {
        Collection<RecipeEntry<SieveRecipe>> recipes = allSieveRecipes();
        if (recipes.isEmpty()) {
            FabricaeExNihilo.LOGGER.warn(
                    "[FEN/JEI] no sieve recipes available yet; the sieve category will be rebuilt when recipe sync completes");
            return List.of();
        }

        Map<String, Group> groups = new LinkedHashMap<>();
        for (RecipeEntry<SieveRecipe> entry : recipes) {
            SieveRecipe recipe = entry.value();
            ItemStack result = recipe.getResult();
            if (result.isEmpty()) {
                continue;
            }

            for (var roll : recipe.getRolls().entrySet()) {
                Item mesh = ItemUtils.getItem(roll.getKey());
                if (mesh == Items.AIR || new ItemStack(mesh).isEmpty()) {
                    continue;
                }
                List<Double> chances = roll.getValue();
                if (chances == null || chances.isEmpty()) {
                    continue;
                }

                String key = groupKey(recipe.getInput(), mesh, recipe.isWaterlogged());
                Group group = groups.computeIfAbsent(key, ignored ->
                        new Group(recipe.getInput(), new ItemStack(mesh), recipe.isWaterlogged()));
                group.outputs.add(new JeiSieveRecipe.Output(result.copy(), chances));
            }
        }

        List<JeiSieveRecipe> pages = new ArrayList<>(groups.size());
        for (Group group : groups.values()) {
            pages.add(new JeiSieveRecipe(group.input, group.mesh, group.waterlogged, group.outputs));
        }
        return pages;
    }

    @SuppressWarnings("unchecked")
    private static Collection<RecipeEntry<SieveRecipe>> allSieveRecipes() {
        // 1. Latest snapshot pushed by Fabric's recipe synchronisation event.
        //    This is the only reliable source on a dedicated server.
        SynchronizedRecipes snapshot = synchronizedRecipes;
        if (snapshot != null) {
            Collection<RecipeEntry<SieveRecipe>> fromSnapshot = snapshot.getAllOfType(ModRecipes.SIEVE);
            if (fromSnapshot != null && !fromSnapshot.isEmpty()) {
                return fromSnapshot;
            }
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return List.of();
        }

        // 2. Live client recipe manager, in case it was populated before the event.
        var networkHandler = client.getNetworkHandler();
        if (networkHandler != null
                && networkHandler.getRecipeManager() instanceof FabricRecipeManager fabric) {
            SynchronizedRecipes synced = fabric.getSynchronizedRecipes();
            if (synced != null) {
                Collection<RecipeEntry<SieveRecipe>> fromManager = synced.getAllOfType(ModRecipes.SIEVE);
                if (!fromManager.isEmpty()) {
                    return fromManager;
                }
            }
        }

        // 3. Integrated-server fallback. Never skip this just because a sync
        //    snapshot exists but happens to contain no FEN sieve recipes.
        var server = client.getServer();
        if (server != null) {
            return server.getRecipeManager().values().stream()
                    .filter(entry -> entry.value().getType() == ModRecipes.SIEVE)
                    .map(entry -> (RecipeEntry<SieveRecipe>) entry)
                    .toList();
        }

        return List.of();
    }

    private static String groupKey(Ingredient input, Item mesh, boolean waterlogged) {
        List<String> inputIds = new ArrayList<>();
        for (var itemEntry : input.getMatchingItems().toList()) {
            inputIds.add(Registries.ITEM.getId(itemEntry.value()).toString());
        }
        Collections.sort(inputIds);
        return String.join(",", inputIds) + "|" + Registries.ITEM.getId(mesh) + "|" + waterlogged;
    }

    private static final class Group {
        private final Ingredient input;
        private final ItemStack mesh;
        private final boolean waterlogged;
        private final List<JeiSieveRecipe.Output> outputs = new ArrayList<>();

        private Group(Ingredient input, ItemStack mesh, boolean waterlogged) {
            this.input = input;
            this.mesh = mesh;
            this.waterlogged = waterlogged;
        }
    }
}
