package wraith.fabricaeexnihilo.api.recipes.witchwater;

import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.WeightedList;

public record WitchWaterWorldRecipe(FluidIngredient fluid, WeightedList results) {

    public WitchWaterWorldRecipe(FluidIngredient fluid, WeightedList results) {
        this.fluid = fluid;
        this.results = results;
    }

    public static WitchWaterWorldRecipeBuilder builder() {
        return new WitchWaterWorldRecipeBuilder();
    }

    static class WitchWaterWorldRecipeBuilder {

        private FluidIngredient fluid = FluidIngredient.EMPTY;
        private WeightedList results = new WeightedList();

        public WitchWaterWorldRecipeBuilder setFluid(FluidIngredient fluid) {
            this.fluid = fluid;
            return this;
        }

        public WitchWaterWorldRecipeBuilder setResults(WeightedList results) {
            this.results = results;
            return this;
        }

        public WitchWaterWorldRecipe build() {
            return new WitchWaterWorldRecipe(fluid, results);
        }

    }

}