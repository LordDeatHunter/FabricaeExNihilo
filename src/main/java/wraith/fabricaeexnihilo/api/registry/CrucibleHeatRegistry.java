package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.api.crafting.BlockIngredient;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleHeatRecipe;

import java.util.Collection;

public interface CrucibleHeatRegistry extends Registry<CrucibleHeatRecipe> {

    default boolean register(BlockIngredient blocks, FluidIngredient fluids, int heat) {
        return register(new CrucibleHeatRecipe(blocks, fluids, heat));
    }

    default boolean register(Tag.Identified<Fluid> tag, int heat) {
        return register(BlockIngredient.EMPTY, new FluidIngredient(tag), heat);
    }

    default boolean register(Block block, int heat) {
        return register(new BlockIngredient(block), FluidIngredient.EMPTY, heat);
    }

    default boolean register(Fluid fluid, int heat) {
        return register(BlockIngredient.EMPTY, new FluidIngredient(fluid), heat);
    }

    int getHeat(Block block);

    int getHeat(Fluid fluid);

    int getHeat(BlockItem item);

    // All recipes, chunked/broken up for pagination
    Collection<CrucibleHeatRecipe> getREIRecipes();

}