package wraith.fabricaeexnihilo.api.recipes;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Lootable;

import java.util.List;

public record SieveRecipe(ItemIngredient mesh,
                          FluidIngredient fluid,
                          ItemIngredient sievable,
                          List<Lootable> loot) {

    public ItemIngredient getMesh() {
        return mesh;
    }

    public FluidIngredient getFluid() {
        return fluid;
    }

    public ItemIngredient getSievable() {
        return sievable;
    }

    public List<Lootable> getLoot() {
        return loot;
    }

    public boolean test(ItemStack mesh, Fluid fluid, ItemStack sievable) {
        return this.mesh.test(mesh) && fluid != null ? this.fluid.test(fluid) : this.fluid.isEmpty() && this.sievable.test(sievable);
    }

    public boolean test(SieveRecipe other) {
        return this.mesh == other.mesh && this.fluid == other.fluid && this.sievable == other.sievable;
    }

}