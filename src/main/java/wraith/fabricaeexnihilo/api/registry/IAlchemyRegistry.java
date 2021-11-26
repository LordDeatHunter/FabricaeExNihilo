package wraith.fabricaeexnihilo.api.registry;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.api.crafting.EntityStack;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Lootable;
import wraith.fabricaeexnihilo.api.recipes.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.Collection;

public interface IAlchemyRegistry extends IRegistry<AlchemyRecipe> {

    AlchemyRecipe getRecipe(FluidVolume reactant, ItemStack catalyst);

    default boolean hasRecipe(FluidVolume reactant, ItemStack catalyst) {
        return getRecipe(reactant, catalyst) != null;
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, Lootable byproduct, int delay, EntityStack spawn) {
        return register(AlchemyRecipe.builder()
                .withReactant(reactant)
                .withCatalyst(catalyst)
                .withByproduct(byproduct)
                .withDelay(delay)
                .withSpawn(spawn)
                .build());
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, ItemStack product, Lootable byproduct, int delay, EntityStack spawn) {
        return register(AlchemyRecipe.builder()
                .withReactant(reactant)
                .withCatalyst(catalyst)
                .withProduct(new ItemMode(product))
                .withByproduct(byproduct)
                .withDelay(delay)
                .withSpawn(spawn)
                .build());
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, FluidVolume product, Lootable byproduct, int delay, EntityStack spawn) {
        return register(AlchemyRecipe.builder()
                .withReactant(reactant)
                .withCatalyst(catalyst)
                .withProduct(new FluidMode(product))
                .withByproduct(byproduct)
                .withDelay(delay)
                .withSpawn(spawn)
                .build());
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, FluidVolume product) {
        return register(AlchemyRecipe.builder()
                .withReactant(reactant)
                .withCatalyst(catalyst)
                .withProduct(new FluidMode(product))
                .build());
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, ItemStack product) {
        return register(AlchemyRecipe.builder()
                .withReactant(reactant)
                .withCatalyst(catalyst)
                .withProduct(new ItemMode(product))
                .build());
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, EntityStack spawn) {
        return register(AlchemyRecipe.builder()
                .withReactant(reactant)
                .withCatalyst(catalyst)
                .withSpawn(spawn)
                .withDelay(20)
                .build());
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, FluidVolume product, EntityStack spawn) {
        return register(reactant, catalyst, product, Lootable.EMPTY, 20, spawn);
    }

    default boolean register(FluidIngredient reactant, ItemIngredient catalyst, ItemStack product, EntityStack spawn) {
        return register(reactant, catalyst, product, Lootable.EMPTY, 20, spawn);
    }

    default boolean register(Fluid reactant, ItemStack catalyst, Fluid product) {
        return register(new FluidIngredient(reactant), new ItemIngredient(catalyst), FluidKeys.get(product).withAmount(FluidAmount.BUCKET));
    }

    default boolean register(Fluid reactant, ItemConvertible catalyst, Fluid product) {
        return register(new FluidIngredient(reactant), new ItemIngredient(catalyst), FluidKeys.get(product).withAmount(FluidAmount.BUCKET));
    }

    default boolean register(Fluid reactant, Tag.Identified<Item> catalyst, Fluid product) {
        return register(new FluidIngredient(reactant), new ItemIngredient(catalyst), FluidKeys.get(product).withAmount(FluidAmount.BUCKET));
    }

    default boolean register(Fluid reactant, ItemStack catalyst, ItemStack product) {
        return register(new FluidIngredient(reactant), new ItemIngredient(catalyst), product);
    }

    default boolean register(Fluid reactant, ItemConvertible catalyst, ItemConvertible product) {
        return register(new FluidIngredient(reactant), new ItemIngredient(catalyst), ItemUtils.asStack(product));
    }

    default boolean register(Tag.Identified<Fluid> reactant, ItemConvertible catalyst, ItemConvertible product) {
        return register(new FluidIngredient(reactant), new ItemIngredient(catalyst), ItemUtils.asStack(product));
    }

    default boolean register(Tag.Identified<Fluid> reactant, ItemStack catalyst, EntityType<?> spawn) {
        return register(new FluidIngredient(reactant), new ItemIngredient(catalyst), new EntityStack(spawn));
    }

    default boolean register(FluidIngredient reactant, ItemStack catalyst, EntityType<?> spawn) {
        return register(reactant, new ItemIngredient(catalyst), new EntityStack(spawn));
    }


    // All recipes, chunked/broken up for pagination
    Collection<AlchemyRecipe> getREIRecipes();

}