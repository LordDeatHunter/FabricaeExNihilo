package wraith.fabricaeexnihilo.api.recipes.barrel;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;

public record LeakingRecipe(ItemIngredient target,
                            FluidIngredient fluid,
                            FluidAmount loss, Block result) {


    public static LeakingRecipeBuilder builder() {
        return new LeakingRecipeBuilder();
    }

    public static class LeakingRecipeBuilder {
        private ItemIngredient target = ItemIngredient.EMPTY;
        private FluidIngredient fluid = FluidIngredient.EMPTY;
        private FluidAmount loss = FluidAmount.ZERO;
        private Block result = Blocks.AIR;

        public LeakingRecipeBuilder withTarget(ItemIngredient target) {
            this.target = target;
            return this;
        }

        public LeakingRecipeBuilder withFluid(FluidIngredient fluid) {
            this.fluid = fluid;
            return this;
        }

        public LeakingRecipeBuilder withLoss(FluidAmount loss) {
            this.loss = loss;
            return this;
        }

        public LeakingRecipeBuilder withResult(Block result) {
            this.result = result;
            return this;
        }

        public LeakingRecipe build() {
            return new LeakingRecipe(target, fluid, loss, result);
        }
    }

}