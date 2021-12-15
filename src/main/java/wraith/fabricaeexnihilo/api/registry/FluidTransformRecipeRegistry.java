package wraith.fabricaeexnihilo.api.registry;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidTransformRecipe;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.util.FluidUtils;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.Collection;

public interface FluidTransformRecipeRegistry extends RecipeRegistry<FluidTransformRecipe> {

    default boolean register(FluidIngredient fluid, ItemIngredient block, BarrelMode result) {
        return register(new FluidTransformRecipe(fluid, block, result));
    }

    default boolean register(Fluid fluid, Block block, Block result) {
        return register(new FluidIngredient(fluid), new ItemIngredient(block), new ItemMode(ItemUtils.asStack(result)));
    }

    default boolean register(Fluid fluid, Block block, FluidVolume result) {
        return register(new FluidIngredient(fluid), new ItemIngredient(block), new FluidMode(result));
    }

    default boolean register(Fluid fluid, Block block, Fluid result) {
        return register(new FluidIngredient(fluid), new ItemIngredient(block), new FluidMode(FluidUtils.asVolume(result)));
    }

    BarrelMode getResult(Fluid contents, Block block);

    default BarrelMode getResult(FluidVolume contents, Block block) {
        return getResult(contents.getRawFluid(), block);
    }

    // All recipes, chunked/broken up for pagination
    Collection<FluidTransformRecipe> getREIRecipes();

}