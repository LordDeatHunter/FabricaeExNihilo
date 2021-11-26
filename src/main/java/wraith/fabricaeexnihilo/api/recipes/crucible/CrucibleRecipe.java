package wraith.fabricaeexnihilo.api.recipes.crucible;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;

public record CrucibleRecipe(ItemIngredient input, FluidVolume output) {
}
