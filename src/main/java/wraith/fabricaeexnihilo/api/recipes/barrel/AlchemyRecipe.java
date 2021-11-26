package wraith.fabricaeexnihilo.api.recipes.barrel;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.api.crafting.EntityStack;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Lootable;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;

public record AlchemyRecipe(FluidIngredient reactant,
                            ItemIngredient catalyst,
                            BarrelMode product,
                            Lootable byproduct,
                            int delay,
                            EntityStack toSpawn) {

    public boolean test(FluidVolume reactant, ItemStack catalyst) {
        return reactant.amount().isGreaterThanOrEqual(FluidAmount.BUCKET) && this.reactant.test(reactant) && this.catalyst.test(catalyst);
    }

    public FluidIngredient getReactant() {
        return reactant;
    }

    public ItemIngredient getCatalyst() {
        return catalyst;
    }

    public BarrelMode getProduct() {
        return product;
    }

    public Lootable getByproduct() {
        return byproduct;
    }

    public int getDelay() {
        return delay;
    }

    public EntityStack getToSpawn() {
        return toSpawn;
    }

    public static AlchemyRecipeBuilder builder() {
        return new AlchemyRecipeBuilder();
    }

    public boolean match(AlchemyRecipe recipe) {
        return this.getReactant() == recipe.getReactant() && this.getCatalyst() == recipe.getCatalyst();
    }

    public static class AlchemyRecipeBuilder {

        private FluidIngredient reactant = FluidIngredient.EMPTY;
        private ItemIngredient catalyst = ItemIngredient.EMPTY;
        private BarrelMode product = new EmptyMode();
        private Lootable byproduct = Lootable.EMPTY;
        private int delay = 0;
        private EntityStack toSpawn = EntityStack.EMPTY;

        private AlchemyRecipeBuilder() {
        }

        public AlchemyRecipeBuilder withReactant(FluidIngredient reactant) {
            this.reactant = reactant;
            return this;
        }

        public AlchemyRecipeBuilder withCatalyst(ItemIngredient catalyst) {
            this.catalyst = catalyst;
            return this;
        }

        public AlchemyRecipeBuilder withProduct(BarrelMode product) {
            this.product = product;
            return this;
        }

        public AlchemyRecipeBuilder withByproduct(Lootable byproduct) {
            this.byproduct = byproduct;
            return this;
        }

        public AlchemyRecipeBuilder withDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public AlchemyRecipeBuilder withSpawn(EntityStack toSpawn) {
            this.toSpawn = toSpawn;
            return this;
        }

        public AlchemyRecipe build() {
            return new AlchemyRecipe(reactant, catalyst, product, byproduct, delay, toSpawn);
        }

    }

}
