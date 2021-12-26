package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.WeightedList;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterWorldRecipe;

import java.util.Collection;
import java.util.Random;

public interface WitchWaterWorldRecipeRegistry extends RecipeRegistry<WitchWaterWorldRecipe> {

    default boolean register(FluidIngredient fluid, WeightedList result) {
        return register(new WitchWaterWorldRecipe(fluid, result));
    }

    boolean isRegistered(Fluid fluid);

    @Nullable
    Block getResult(Fluid fluid, Random rand);

    @Nullable
    WeightedList getAllResults(Fluid fluid);

    Collection<WitchWaterWorldRecipe> getREIRecipes();

}