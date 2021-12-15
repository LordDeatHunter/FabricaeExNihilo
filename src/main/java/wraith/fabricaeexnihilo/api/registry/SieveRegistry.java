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
import wraith.fabricaeexnihilo.api.crafting.Lootable;
import wraith.fabricaeexnihilo.api.recipes.SieveRecipe;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface SieveRegistry extends Registry<SieveRecipe> {

    List<ItemStack> getResult(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable, @Nullable PlayerEntity player, Random rand);

    List<Lootable> getAllResults(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable);

    boolean isValidRecipe(ItemStack mesh, @Nullable Fluid fluid, ItemStack sievable);

    boolean isValidMesh(ItemStack mesh);

    default boolean register(ItemIngredient mesh, FluidIngredient fluid, ItemIngredient sievable, Collection<Lootable> loot) {
        return register(new SieveRecipe(mesh, fluid, sievable, loot.stream().toList()));
    }

    default boolean register(ItemIngredient mesh, FluidIngredient fluid, ItemIngredient sievable, Lootable... loot) {
        return register(mesh, fluid, sievable, List.of(loot));
    }

    default boolean register(ItemIngredient mesh, ItemIngredient sievable, Lootable... loot) {
        return register(mesh, FluidIngredient.EMPTY, sievable, List.of(loot));
    }

    default boolean register(ItemStack mesh, Fluid fluid, ItemStack sievable, Lootable... loot) {
        return register(new ItemIngredient(mesh), new FluidIngredient(fluid), new ItemIngredient(sievable), List.of(loot));
    }

    default boolean register(ItemConvertible mesh, Fluid fluid, ItemConvertible sievable, Lootable... loot) {
        return register(new ItemIngredient(mesh), new FluidIngredient(fluid), new ItemIngredient(sievable), loot);
    }

    default boolean register(ItemConvertible mesh, Fluid fluid, Identifier sievable, Lootable... loot) {
        return register(new ItemIngredient(mesh), new FluidIngredient(fluid), new ItemIngredient(net.minecraft.util.registry.Registry.ITEM.get(sievable)), List.of(loot));
    }

    default boolean register(ItemConvertible mesh, ItemConvertible sievable, Lootable... loot) {
        return register(new ItemIngredient(mesh), FluidIngredient.EMPTY, new ItemIngredient(sievable), List.of(loot));
    }

    default boolean register(ItemConvertible mesh, Tag.Identified<Item> sievable, Lootable... loot) {
        return register(new ItemIngredient(mesh), FluidIngredient.EMPTY, new ItemIngredient(sievable), List.of(loot));
    }

    default boolean register(Identifier mesh, FluidIngredient fluid, ItemIngredient sievable, Collection<Lootable> loot) {
        return register(new SieveRecipe(new ItemIngredient(net.minecraft.util.registry.Registry.ITEM.get(mesh)), fluid, sievable, loot.stream().toList()));
    }

    default boolean register(Identifier mesh, FluidIngredient fluid, ItemIngredient sievable, Lootable... loot) {
        return register(new ItemIngredient(net.minecraft.util.registry.Registry.ITEM.get(mesh)), fluid, sievable, List.of(loot));
    }

    default boolean register(Identifier mesh, ItemIngredient sievable, Lootable... loot) {
        return register(mesh, FluidIngredient.EMPTY, sievable, loot);
    }

    default boolean register(Identifier mesh, ItemConvertible sievable, Lootable... loot) {
        return register(mesh, new ItemIngredient(sievable), loot);
    }

    default boolean register(ItemConvertible mesh, Identifier sievable, Lootable... loot) {
        return register(mesh, net.minecraft.util.registry.Registry.ITEM.get(sievable), loot);
    }

    Collection<SieveRecipe> getAllRecipes();

    // All recipes, chunked/broken up for pagination
    Collection<SieveRecipe> getREIRecipes();
}

