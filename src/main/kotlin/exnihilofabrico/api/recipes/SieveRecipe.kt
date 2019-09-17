package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.Lootable
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient

data class SieveRecipe(
        val mesh: Ingredient,
        val fluid: FluidIngredient?,
        val sievable: Ingredient,
        val loot: MutableCollection<Lootable>
) {
    fun test(mesh: ItemStack, fluid: Fluid?, sievable: ItemStack) =
        this.mesh.test(mesh) && (this.fluid?.test(fluid) ?: (fluid == Fluids.EMPTY || fluid == null)) && this.sievable.test(sievable)
    fun test(other: SieveRecipe) = this.mesh == other.mesh && this.fluid == other.fluid && this.sievable == other.sievable
}