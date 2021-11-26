package wraith.fabricaeexnihilo.api.registry;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleRecipe;

import java.util.Collection;

public interface ICrucibleRegistry extends IRegistry<CrucibleRecipe> {

    default boolean register(ItemIngredient input, FluidVolume output) {
        return register(new CrucibleRecipe(input, output));
    }

    default boolean register(ItemConvertible input, FluidVolume output) {
        return register(new ItemIngredient(input), output);
    }

    default boolean register(Tag.Identified<Item> input, FluidVolume output) {
        return register(new ItemIngredient(input), output);
    }

    default boolean register(ItemIngredient input, Fluid fluid, int amount) {
        return register(input, FluidKeys.get(fluid).withAmount(FluidAmount.of1620(amount)));
    }

    default boolean register(ItemConvertible input, Fluid fluid, int amount) {
        return register(new ItemIngredient(input), FluidKeys.get(fluid).withAmount(FluidAmount.of1620(amount)));
    }

    default boolean register(Tag.Identified<Item> input, Fluid fluid, int amount) {
        return register(new ItemIngredient(input), FluidKeys.get(fluid).withAmount(FluidAmount.of1620(amount)));
    }

    FluidVolume getResult(Item item);

    // All recipes, chunked/broken up for pagination
    Collection<CrucibleRecipe> getREIRecipes();

}