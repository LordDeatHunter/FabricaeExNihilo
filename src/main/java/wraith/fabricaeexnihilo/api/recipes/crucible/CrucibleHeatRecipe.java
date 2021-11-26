package wraith.fabricaeexnihilo.api.recipes.crucible;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;

public record CrucibleHeatRecipe(ItemIngredient ingredient, FluidIngredient fluid, int value) {

    public boolean test(Block block) {
        return block instanceof FluidBlock ? test(block) : ingredient.test(block);
    }

    public boolean test(BlockState state) {
        return test(state.getBlock());
    }

    public boolean test(Fluid fluid) {
        return this.fluid.test(fluid);
    }

    public boolean test(FluidBlock fluid) {
        return this.fluid.test(fluid);
    }

    public boolean test(FluidState fluid) {
        return this.fluid.test(fluid);
    }

    public boolean test(Item item) {
        return ingredient.test(item);
    }

    public boolean test(ItemStack stack) {
        return test(stack.getItem());
    }

}
