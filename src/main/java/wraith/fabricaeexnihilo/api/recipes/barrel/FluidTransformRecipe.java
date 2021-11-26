package wraith.fabricaeexnihilo.api.recipes.barrel;

import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;

public record FluidTransformRecipe(FluidIngredient inBarrel, ItemIngredient catalyst, BarrelMode result) {
}
