package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Loot;
import wraith.fabricaeexnihilo.api.recipes.SieveRecipe;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface SieveRecipeRegistry extends RecipeRegistry<SieveRecipe> {

    List<ItemStack> getResult(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable, @Nullable PlayerEntity player, Random rand);

    List<Loot> getAllResults(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable);

    boolean isValidRecipe(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable);

    boolean isValidMesh(ItemStack mesh);

    default boolean register(ItemIngredient mesh, FluidIngredient fluid, ItemIngredient sievable, Collection<Loot> loot) {
        return register(new SieveRecipe(mesh, fluid, sievable, loot.stream().toList()));
    }

    default boolean register(ItemIngredient mesh, FluidIngredient fluid, ItemIngredient sievable, Loot... loot) {
        return register(mesh, fluid, sievable, List.of(loot));
    }

    default boolean register(ItemIngredient mesh, ItemIngredient sievable, Loot... loot) {
        return register(mesh, FluidIngredient.EMPTY, sievable, List.of(loot));
    }

    default boolean register(ItemStack mesh, Fluid fluid, ItemStack sievable, Loot... loot) {
        return register(new ItemIngredient(mesh), new FluidIngredient(fluid), new ItemIngredient(sievable), List.of(loot));
    }

    default boolean register(ItemConvertible mesh, Fluid fluid, ItemConvertible sievable, Loot... loot) {
        return register(new ItemIngredient(mesh), new FluidIngredient(fluid), new ItemIngredient(sievable), loot);
    }

    default boolean register(ItemConvertible mesh, Fluid fluid, Identifier sievable, Loot... loot) {
        return register(new ItemIngredient(mesh), new FluidIngredient(fluid), new ItemIngredient(net.minecraft.util.registry.Registry.ITEM.get(sievable)), List.of(loot));
    }

    default boolean register(ItemConvertible mesh, ItemConvertible sievable, Loot... loot) {
        return register(new ItemIngredient(mesh), FluidIngredient.EMPTY, new ItemIngredient(sievable), List.of(loot));
    }

    default boolean register(ItemConvertible mesh, Tag.Identified<Item> sievable, Loot... loot) {
        return register(new ItemIngredient(mesh), FluidIngredient.EMPTY, new ItemIngredient(sievable), List.of(loot));
    }

    default boolean register(Identifier mesh, FluidIngredient fluid, ItemIngredient sievable, Collection<Loot> loot) {
        return register(new SieveRecipe(new ItemIngredient(net.minecraft.util.registry.Registry.ITEM.get(mesh)), fluid, sievable, loot.stream().toList()));
    }

    default boolean register(Identifier mesh, FluidIngredient fluid, ItemIngredient sievable, Loot... loot) {
        return register(new ItemIngredient(net.minecraft.util.registry.Registry.ITEM.get(mesh)), fluid, sievable, List.of(loot));
    }

    default boolean register(Identifier mesh, ItemIngredient sievable, Loot... loot) {
        return register(mesh, FluidIngredient.EMPTY, sievable, loot);
    }

    default boolean register(Identifier mesh, ItemConvertible sievable, Loot... loot) {
        return register(mesh, new ItemIngredient(sievable), loot);
    }

    default boolean register(ItemConvertible mesh, Identifier sievable, Loot... loot) {
        return register(mesh, net.minecraft.util.registry.Registry.ITEM.get(sievable), loot);
    }

    Collection<SieveRecipe> getAllRecipes();

    // All recipes, chunked/broken up for pagination
    Collection<SieveRecipe> getREIRecipes();
}

